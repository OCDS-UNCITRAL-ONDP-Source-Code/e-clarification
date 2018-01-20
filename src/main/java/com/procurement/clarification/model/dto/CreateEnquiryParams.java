package com.procurement.clarification.model.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CreateEnquiryParams {
    private final String cpid;
    private final String stage;
    private final String owner;
    private final CreateEnquiryDto dataDto;
    @Setter
    private LocalDateTime date;

    public CreateEnquiryParams(final String cpid,
                               final String stage,
                               final LocalDateTime date,
                               final String owner,
                               final CreateEnquiryDto dataDto) {
        this.cpid = cpid;
        this.stage = stage;
        this.date = date;
        this.owner = owner;
        this.dataDto = dataDto;
    }
}