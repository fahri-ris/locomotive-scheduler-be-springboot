package com.ris.locomotivescheduler.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Scheduled(cron = "${cron.schedule.every-10-seconds}")
    public void sendNewLocomotive(){
        // method to send new loco
        log.info("scheduler jalan");
    }

    @Scheduled(cron = "${cron.schedule.every-1-hours}")
    public void addNewLocomotive(){
        // method to save summary loco & send to telegram
        log.info("scheduler summary jalan");
    }
}
