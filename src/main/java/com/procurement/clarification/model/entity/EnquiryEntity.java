package com.procurement.clarification.model.entity;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("clarification_enquiry")
@Getter
@Setter
public class EnquiryEntity {
    @PrimaryKeyColumn(name = "oc_id", type = PrimaryKeyType.PARTITIONED)
    private String ocId;

    @PrimaryKeyColumn(value = "is_answered", type = PrimaryKeyType.CLUSTERED)
    private Boolean isAnswered;

    @PrimaryKeyColumn(value = "enquiry_id", type = PrimaryKeyType.CLUSTERED)
    private UUID enquiryId;

    @Column(value = "json_data")
    private String jsonData;
}
