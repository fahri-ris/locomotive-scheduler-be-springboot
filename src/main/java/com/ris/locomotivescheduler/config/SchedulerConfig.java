package com.ris.locomotivescheduler.config;

import com.ris.locomotivescheduler.service.LocomotiveInfoService;
import com.ris.locomotivescheduler.service.SummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
public class SchedulerConfig {
    private final LocomotiveInfoService locomotiveInfoService;
    private final SummaryService summaryService;

    @Autowired
    public SchedulerConfig(LocomotiveInfoService locomotiveInfoService, SummaryService summaryService) {
        this.locomotiveInfoService = locomotiveInfoService;
        this.summaryService = summaryService;
    }

    @Scheduled(cron = "${cron.schedule.every-10-seconds}")
    public void sendNewLocomotive(){
        // method to send new loco
        locomotiveInfoService.postLocomotiveInfo();
    }

    @Scheduled(cron = "${cron.schedule.every-1-hours}")
    public void addNewLocomotive(){
        // method to save summary loco & send to telegram
        summaryService.getLocoSummary();
    }
}
