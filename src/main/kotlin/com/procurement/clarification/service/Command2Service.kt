package com.procurement.clarification.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.infrastructure.dto.ApiResponse
import com.procurement.clarification.infrastructure.handler.find.equiryids.FindEnquiryIdsHandler
import com.procurement.clarification.infrastructure.handler.get.enquirybyids.GetEnquiryByIdsHandler
import com.procurement.clarification.model.dto.bpe.Command2Type
import com.procurement.clarification.model.dto.bpe.errorResponse
import com.procurement.clarification.model.dto.bpe.getAction
import com.procurement.clarification.model.dto.bpe.getId
import com.procurement.clarification.model.dto.bpe.getVersion
import org.springframework.stereotype.Service

@Service
class Command2Service(
    private val logger: Logger,
    private val findEnquiryIdsHandler: FindEnquiryIdsHandler,
    private val getEnquiryByIdsHandler: GetEnquiryByIdsHandler
) {

    fun execute(request: JsonNode): ApiResponse {

        val version = request.getVersion()
            .doOnError { versionError ->
                val id = request.getId()
                    .doOnError { _ -> return errorResponse(fail = versionError) }
                    .get
                return errorResponse(fail = versionError, id = id)
            }
            .get

        val id = request.getId()
            .doOnError { error -> return errorResponse(fail = error, version = version) }
            .get

        val action = request.getAction()
            .doOnError { error -> return errorResponse(id = id, version = version, fail = error) }
            .get

        val response: ApiResponse = when (action) {
            Command2Type.FIND_ENQUIRY_IDS -> findEnquiryIdsHandler.handle(node = request)
            Command2Type.GET_ENQUIRY_BY_IDS -> getEnquiryByIdsHandler.handle(node = request)
        }

        logger.info("DataOfResponse: '$response'.")
        return response
    }
}
