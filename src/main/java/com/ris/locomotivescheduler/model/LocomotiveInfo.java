package com.ris.locomotivescheduler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Document(collection = "locomotive_info")
@Builder
public class LocomotiveInfo {
    @Id
    private String code;
    private String name;
    private String dimension;
    private String status;
    private Instant createdAt;
}
