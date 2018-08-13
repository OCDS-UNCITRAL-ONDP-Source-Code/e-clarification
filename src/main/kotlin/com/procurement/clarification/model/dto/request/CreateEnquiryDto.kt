package com.procurement.clarification.model.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.clarification.model.dto.ocds.Address
import com.procurement.clarification.model.dto.ocds.ContactPoint
import com.procurement.clarification.model.dto.ocds.Identifier
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class CreateEnquiryDto @JsonCreator constructor(

    @field:Valid
    @field:NotNull
    val enquiry: EnquiryCreate
)

data class EnquiryCreate @JsonCreator constructor(

    @field:Valid
    @field:NotNull
    val author: OrganizationReferenceCreate,

    @field:NotNull
    val title: String,

    @field:NotNull
    val description: String,

    val relatedItem: String?,

    val relatedLot: String?
)

data class OrganizationReferenceCreate @JsonCreator constructor(

    @field:NotNull
    val name: String,

    @field:Valid
    @field:NotNull
    val identifier: IdentifierCreate,

    @field:Valid
    @field:NotNull
    val address: Address,

    @field:Valid
    val additionalIdentifiers: Set<Identifier>?,

    @field:Valid
    @field:NotNull
    val contactPoint: ContactPoint
)

data class IdentifierCreate @JsonCreator constructor(

    @field:NotNull
    val scheme: String,

    @field:NotNull
    val id: String,

    @field:NotNull
    val legalName: String,

    val uri: String?
)


