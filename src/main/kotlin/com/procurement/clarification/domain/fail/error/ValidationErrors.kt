package com.procurement.clarification.domain.fail.error

import com.procurement.clarification.application.service.Logger
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid

sealed class ValidationErrors(
    numberError: String,
    override val description: String,
    val entityId: String? = null
) : Fail.Error(prefix = "VR.COM-") {

    override val code: String = prefix + numberError

    override fun logging(logger: Logger) {
        logger.error(message = message)
    }

    class EnquiriesNotFoundOnFindEnquiryIds(val cpid: Cpid, val ocid: Ocid) : ValidationErrors(
        numberError = "8.2.1",
        description = "Enquiries not found by cpid='$cpid' and ocid='$ocid'."
    )
}
