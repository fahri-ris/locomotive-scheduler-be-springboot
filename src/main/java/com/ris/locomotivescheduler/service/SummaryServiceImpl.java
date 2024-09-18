package com.ris.locomotivescheduler.service;

import com.ris.locomotivescheduler.dto.SummaryResponse;
import com.ris.locomotivescheduler.model.Summary;
import com.ris.locomotivescheduler.repository.SummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SummaryServiceImpl implements SummaryService {
    private final SummaryRepository summaryRepository;

    @Autowired
    public SummaryServiceImpl(SummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    @Override
    public List<SummaryResponse> getAllSummary() {
        List<Summary> summaries = summaryRepository.findAll();
        List<SummaryResponse> summaryResponses = summaries.stream()
                .map(summary -> SummaryResponse.builder()
                        .id(summary.getId())
                        .status(summary.getStatus())
                        .total(summary.getTotal())
                        .createdAt(summary.getCreatedAt())
                        .build())
                .toList();

        return summaryResponses;
    }
}
