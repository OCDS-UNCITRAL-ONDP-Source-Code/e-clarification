package com.procurement.clarification.service

import com.procurement.clarification.dao.RulesDao
import com.procurement.clarification.exception.ErrorException
import com.procurement.clarification.exception.ErrorType
import org.springframework.stereotype.Service

interface RulesService {

    fun getInterval(country: String, method: String): Long

    fun getOffset(country: String, method: String): Long

    fun getOffsetExtended(country: String, method: String): Long

    fun getIntervalBefore(country: String, pmd: String): Long

}

@Service
class RulesServiceImpl(private val rulesDao: RulesDao) : RulesService {

    override fun getInterval(country: String, method: String): Long {
        return rulesDao.getValue(country, method, PARAMETER_INTERVAL)?.toLongOrNull()
                ?: throw ErrorException(ErrorType.INTERVAL_RULES_NOT_FOUND)
    }

    override fun getOffset(country: String, method: String): Long {
        return rulesDao.getValue(country, method, PARAMETER_OFFSET)?.toLongOrNull()
                ?: throw ErrorException(ErrorType.OFFSET_RULES_NOT_FOUND)
    }

    override fun getOffsetExtended(country: String, method: String): Long {
        return rulesDao.getValue(country, method, PARAMETER_OFFSET_EXTENDED)?.toLongOrNull()
                ?: throw ErrorException(ErrorType.OFFSET_RULES_NOT_FOUND)
    }

    override fun getIntervalBefore(country: String, pmd: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val PARAMETER_INTERVAL = "interval"
        private const val PARAMETER_OFFSET = "offset"
        private const val PARAMETER_OFFSET_EXTENDED = "offsetExtended"
    }
}
