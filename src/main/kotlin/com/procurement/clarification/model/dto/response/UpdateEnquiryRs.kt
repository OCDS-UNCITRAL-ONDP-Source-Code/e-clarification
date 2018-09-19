package com.procurement.clarification.model.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.clarification.model.dto.ocds.Enquiry


@JsonInclude(JsonInclude.Include.NON_NULL)
data class UpdateEnquiryRs(

        @get:JsonProperty("allAnswered")
        val allAnswered: Boolean?,

        val enquiry: Enquiry
)