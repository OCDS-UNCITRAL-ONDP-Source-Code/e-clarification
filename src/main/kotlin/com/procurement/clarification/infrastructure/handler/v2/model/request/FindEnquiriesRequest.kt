package com.procurement.clarification.infrastructure.handler.v2.model.request


import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class FindEnquiriesRequest(
    @param:JsonProperty("cpid") @field:JsonProperty("cpid") val cpid: String,
    @param:JsonProperty("ocid") @field:JsonProperty("ocid") val ocid: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("isAnswer") @get:JsonProperty("isAnswer") val isAnswer: Boolean?
)
