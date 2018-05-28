package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.params.CreateEnquiryParams;
import com.procurement.clarification.model.dto.params.UpdateEnquiryParams;
import org.springframework.stereotype.Service;

@Service
public interface EnquiryService {

    ResponseDto createEnquiry(CreateEnquiryParams params);

    ResponseDto createAnswer(UpdateEnquiryParams params);

    ResponseDto checkEnquiries(String cpId, String stage);
}