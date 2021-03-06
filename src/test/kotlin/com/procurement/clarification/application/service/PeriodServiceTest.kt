package com.procurement.clarification.application.service

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.clarification.application.model.dto.params.CreateEnquiryPeriodParams
import com.procurement.clarification.application.repository.period.PeriodRepository
import com.procurement.clarification.application.repository.period.model.PeriodEntity
import com.procurement.clarification.domain.extension.format
import com.procurement.clarification.domain.model.Cpid
import com.procurement.clarification.domain.model.Ocid
import com.procurement.clarification.domain.model.enums.OperationType
import com.procurement.clarification.domain.model.enums.ProcurementMethod
import com.procurement.clarification.infrastructure.handler.v2.model.response.CreateEnquiryPeriodResult
import com.procurement.clarification.lib.functional.MaybeFail
import com.procurement.clarification.lib.functional.asSuccess
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import java.util.*

class PeriodServiceTest {

    private lateinit var periodService: PeriodService
    private lateinit var periodRepository: PeriodRepository
    private lateinit var rulesService: RulesService

    companion object {
        private const val COUNTRY = "MD"
        private val PMD = ProcurementMethod.CF
        private val OPERATION_TYPE = OperationType.CREATE_PCR
        private val CPID = Cpid.tryCreateOrNull("ocds-t1s2t3-MD-1565251033096")!!
        private val OCID = Ocid.tryCreateOrNull("ocds-b3wdp1-MD-1581509539187-EV-1581509653044")!!

        private const val FORMAT_PATTERN = "uuuu-MM-dd'T'HH:mm:ss'Z'"
        private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_PATTERN)
            .withResolverStyle(ResolverStyle.STRICT)
        private val RECEIVED_START_DATE = LocalDateTime.parse("2020-02-12T08:49:55Z", FORMATTER)
        private val RECEIVED_END_DATE = LocalDateTime.parse("2020-02-22T08:49:55Z", FORMATTER)
        private val PERIOD_SHIFT = Duration.ofSeconds(Duration.ofDays(2).seconds)
    }

    @BeforeEach
    fun init() {
        periodRepository = mock()
        rulesService = mock()
        periodService = PeriodService(periodRepository, rulesService)
    }

    @Nested
    inner class CreateEnquiryPeriod {

        @Test
        fun createEnquiryPeriod_success() {
            val params = getParams()

            whenever(rulesService.getPeriodShift(country = params.country, pmd = params.pmd))
                .thenReturn(PERIOD_SHIFT.asSuccess())

            whenever(periodRepository.save(any()))
                .thenReturn(MaybeFail.none())

            val actual = periodService.createEnquiryPeriod(params).get
            val expected = CreateEnquiryPeriodResult(
                CreateEnquiryPeriodResult.Tender(
                    CreateEnquiryPeriodResult.Tender.EnquiryPeriod(
                        startDate = params.tender.tenderPeriod.startDate,
                        endDate = LocalDateTime.parse("2020-02-20T08:49:55Z", FORMATTER)
                    )
                )
            )

            assertEquals(expected, actual)
        }

        @Test
        fun save_success() {
            val params = getParams()

            whenever(rulesService.getPeriodShift(country = params.country, pmd = params.pmd))
                .thenReturn(PERIOD_SHIFT.asSuccess())

            whenever(periodRepository.save(any()))
                .thenReturn(MaybeFail.none())

            periodService.createEnquiryPeriod(params).get

            verify(periodRepository).save(period = PeriodEntity(
                cpid = params.cpid,
                ocid = params.ocid,
                owner = params.owner.toString(),
                startDate = RECEIVED_START_DATE,
                endDate = LocalDateTime.parse("2020-02-20T08:49:55Z", FORMATTER)
            )
            )
        }

        private fun getParams() = CreateEnquiryPeriodParams.tryCreate(
            cpid = CPID.toString(),
            ocid = OCID.toString(),
            owner = UUID.randomUUID().toString(),
            pmd = PMD.key,
            operationType = OPERATION_TYPE.key,
            country = COUNTRY,
            tender = CreateEnquiryPeriodParams.Tender(
                CreateEnquiryPeriodParams.Tender.TenderPeriod.tryCreate(
                    startDate = RECEIVED_START_DATE.format(),
                    endDate = RECEIVED_END_DATE.format()
                ).get
            )
        ).get
    }
}