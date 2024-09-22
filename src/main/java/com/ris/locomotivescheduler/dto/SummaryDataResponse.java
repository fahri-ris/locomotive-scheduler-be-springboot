package com.ris.locomotivescheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryDataResponse {
    private Integer poor;
    private Integer good;
    private Integer excelent;
    private String month;
}
