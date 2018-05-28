package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.params.PeriodParams;
import com.procurement.clarification.model.entity.PeriodEntity;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public interface PeriodService {

    ResponseDto calculateAndSavePeriod(PeriodParams params);

    void checkDateInPeriod(LocalDateTime localDateTime, String cpId, String stage);

    PeriodEntity getPeriod(String cpId, String stage);
}