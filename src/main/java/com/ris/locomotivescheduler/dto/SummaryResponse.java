package com.ris.locomotivescheduler.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryResponse {
    private List<SummaryDataResponse> dataset = new ArrayList<>();
    private Integer totalPoor;
    private Integer totalGood;
    private Integer totalExcelent;
}