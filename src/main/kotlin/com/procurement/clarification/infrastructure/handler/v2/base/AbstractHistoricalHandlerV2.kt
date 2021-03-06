package com.procurement.clarification.infrastructure.handler.v2.base

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.application.service.Transform
import com.procurement.clarification.domain.extension.nowDefaultUTC
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.infrastructure.api.ApiVersion
import com.procurement.clarification.infrastructure.api.v2.ApiResponseV2
import com.procurement.clarification.infrastructure.api.v2.errorResponse
import com.procurement.clarification.infrastructure.api.v2.getAction
import com.procurement.clarification.infrastructure.api.v2.getId
import com.procurement.clarification.infrastructure.api.v2.getVersion
import com.procurement.clarification.infrastructure.bind.api.Action
import com.procurement.clarification.infrastructure.bind.api.CommandId
import com.procurement.clarification.infrastructure.handler.Handler
import com.procurement.clarification.infrastructure.handler.HistoryRepository
import com.procurement.clarification.infrastructure.repository.history.model.HistoryEntity
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.utils.toJson

abstract class AbstractHistoricalHandlerV2<ACTION : Action, R>(
    private val target: Class<R>,
    private val transform: Transform,
    private val historyRepository: HistoryRepository,
    private val logger: Logger
) : Handler<ACTION, ApiResponseV2> {

    override fun handle(node: JsonNode): ApiResponseV2 {
        val version = node.getVersion()
            .onFailure {
                return errorResponse(fail = it.reason, version = ApiVersion.NaN, id = CommandId.NaN, logger = logger)
            }
        val id = node.getId()
            .onFailure {
                return errorResponse(fail = it.reason, version = version, id = CommandId.NaN, logger = logger)
            }

        val action = node.getAction()
            .onFailure {
                return errorResponse(fail = it.reason, version = version, id = id, logger = logger)
            }

        val history = historyRepository.getHistory(id, action)
            .onFailure {
                return errorResponse(fail = it.reason, version = version, id = id, logger = logger)
            }

        if (history != null) {
            val result = transform.tryDeserialization(value = history, target = target)
                .mapFailure {
                    Fail.Incident.Database.Parsing(
                        column = "json_data",
                        value = history,
                        exception = it.exception
                    )
                }
                .onFailure {
                    return errorResponse(fail = it.reason, id = id, version = version, logger = logger)
                }
            return ApiResponseV2.Success(version = version, id = id, result = result)
        }

        return execute(node)
            .onFailure {
                return errorResponse(fail = it.reason, version = version, id = id, logger = logger)
            }
            .let { result ->
                if (result != null) {
                    val historyEntity = HistoryEntity(
                        commandId = id,
                        action = action,
                        date = nowDefaultUTC(),
                        data = toJson(result)
                    )
                    historyRepository.saveHistory(historyEntity)
                }
                if (logger.isDebugEnabled)
                    logger.debug("${action.key} has been executed. Result: '${transform.trySerialization(result)}'")

                ApiResponseV2.Success(version = version, id = id, result = result)
            }
    }

    abstract fun execute(node: JsonNode): Result<R?, Fail>
}

