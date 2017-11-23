package com.procurement.clarification.service;

import com.procurement.clarification.model.dto.AuthorDto;
import com.procurement.clarification.model.dto.EnquiryPeriodDto;
import com.procurement.clarification.model.dto.PeriodDataDto;
import com.procurement.clarification.model.dto.TenderPeriodDto;
import com.procurement.clarification.model.entity.EnquiryPeriodEntity;
import com.procurement.clarification.repository.EnquiryPeriodRepository;
import com.procurement.clarification.repository.RulesRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EnquiryPeriodServiceTest {

    private final static String ENQUIRY_ID = "44e128a5-ac7a-4c9a-be4c-224b6bf81b20";
    private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    private final static AuthorDto AUTHOR_DTO = new AuthorDto("Author_name", "author_id");
    private final static String TITLE = "title";
    private final static String DESCRIPTION = "description";
    private final static String ANSWER = "anybody answer";
    private final static String RELATED_ID = "related_id";
    private final static String RELATED_LOT = "related_lot";
    private final static String THREAD_ID = "thread_id";
    private static final String OCID = "ocds-213czf-000-00001";



   private final EnquiryPeriodRepository enquiryPeriodRepository = mock(EnquiryPeriodRepository.class);
   private final RulesRepository rulesRepository = mock(RulesRepository.class);
   private final ConversionService conversionService = mock(ConversionService.class);

   private final EnquiryPeriodServiceImpl enquiryPeriodService = new EnquiryPeriodServiceImpl(enquiryPeriodRepository,
                                                                                       rulesRepository,
                                                                                       conversionService);


    @Test
    @DisplayName("save EnquiryPeriod")
    void saveEnquiryPeriodTest() {




        EnquiryPeriodDto enquiryPeriodDto = new EnquiryPeriodDto(OCID,LOCAL_DATE_TIME,LOCAL_DATE_TIME);
        EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();
        enquiryPeriodEntity.setOcId(OCID);
        enquiryPeriodEntity.setStartDate(LOCAL_DATE_TIME);
        enquiryPeriodEntity.setEndDate(LOCAL_DATE_TIME);

        when(conversionService.convert(enquiryPeriodDto,EnquiryPeriodEntity.class)).thenReturn(enquiryPeriodEntity);

        enquiryPeriodService.saveEnquiryPeriod(enquiryPeriodDto);

        verify(enquiryPeriodRepository,times(1)).save(enquiryPeriodEntity);

    }

    @Test
    @DisplayName("check interval")
    void checkInterval(){
        LocalDateTime start = LOCAL_DATE_TIME.minusDays(5);
        LocalDateTime end = LOCAL_DATE_TIME.plusDays(5);
        Long interval = 5L;

        assertTrue(enquiryPeriodService.checkInterval(start,end,interval));

        start = LOCAL_DATE_TIME.plusDays(1);
        end = LOCAL_DATE_TIME.plusDays(4);
        interval=4L;

        assertFalse(enquiryPeriodService.checkInterval(start,end,interval));

    }

    @Test
    @DisplayName("Check Period and Save")
    void calculateAndSaveEnquiryPeriodTest(){

        LocalDateTime start = LOCAL_DATE_TIME.minusDays(5);
        LocalDateTime end = LOCAL_DATE_TIME.plusDays(5);
        String country = "country";
        String details = "details";

        TenderPeriodDto tenderPeriodDto = new TenderPeriodDto(start,end);

        PeriodDataDto dataDto = new PeriodDataDto(OCID,country,details,tenderPeriodDto);



        EnquiryPeriodEntity enquiryPeriodEntity = new EnquiryPeriodEntity();
        enquiryPeriodEntity.setOcId(OCID);
        enquiryPeriodEntity.setStartDate(start);
        enquiryPeriodEntity.setEndDate(end);

        when(rulesRepository.getValue(dataDto.getCountry(),dataDto.getProcurementMethodDetails(),"offset")).thenReturn("2");
        when(rulesRepository.getValue(dataDto.getCountry(),dataDto.getProcurementMethodDetails(),"interval")).thenReturn("3");


        when(conversionService.convert(dataDto, EnquiryPeriodEntity.class)).thenReturn(enquiryPeriodEntity);

        enquiryPeriodService.calculateAndSaveEnquiryPeriod(dataDto);

        verify(enquiryPeriodRepository,times(1)).save(enquiryPeriodEntity);

    }
}
