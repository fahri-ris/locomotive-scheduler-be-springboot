package com.ris.locomotivescheduler.dto;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryResponse {
    Integer id;
    String status;
    Integer total;
    Instant createdAt;
}