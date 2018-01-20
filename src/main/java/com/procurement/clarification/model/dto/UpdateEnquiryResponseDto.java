package com.procurement.clarification.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
        "allAnswers",
        "enquiry"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateEnquiryResponseDto {

    @JsonProperty("allAnswered")
    private Boolean allAnswered;

    @NotNull
    @JsonProperty("enquiry")
    private final EnquiryDto enquiry;

    @JsonCreator
    public UpdateEnquiryResponseDto(
            @JsonProperty("allAnswers") final Boolean allAnswered,
            @JsonProperty("enquiry") final EnquiryDto enquiry) {
        this.allAnswered = allAnswered;
        this.enquiry = enquiry;
    }
}