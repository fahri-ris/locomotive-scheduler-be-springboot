package com.ris.locomotivescheduler.service;

import com.ris.locomotivescheduler.dto.SummaryResponse;

import java.util.List;

public interface SummaryService {
    SummaryResponse getAllSummary();
    void getLocoSummary();
}
