package com.procurement.clarification.model.dto.params

import com.procurement.clarification.model.dto.request.CreateEnquiryDto
import java.time.LocalDateTime

data class CreateEnquiryParams(
        val cpId: String,
        val stage: String,
        val dateTime: LocalDateTime,
        val data: CreateEnquiryDto
)
