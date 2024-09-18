package com.ris.locomotivescheduler.controller;

import com.ris.locomotivescheduler.dto.SummaryResponse;
import com.ris.locomotivescheduler.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {
    private SummaryService summaryService;

    @Autowired
    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping()
    public ResponseEntity<List<SummaryResponse>> getAllSummary(){
        return ResponseEntity.ok(summaryService.getAllSummary());
    }
}
