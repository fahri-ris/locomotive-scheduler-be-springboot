package com.ris.locomotivescheduler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "locomotive_infos")
@Builder
public class LocomotiveInfo {
    @Id
    @Field("_id")
    private String locoCode;

    @Field("loco_name")
    private String locoName;

    @Field("loco_dimension")
    private String locoDimension;

    @Field("loco_status")
    private String locoStatus;

    @Field("created_at")
    private String createdAt;
}
