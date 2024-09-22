package com.ris.locomotivescheduler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ris.locomotivescheduler.dto.KafkaResponse;
import com.ris.locomotivescheduler.model.LocomotiveInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class LocomotiveInfoServiceImpl implements LocomotiveInfoService{
    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    public LocomotiveInfoServiceImpl(ObjectMapper objectMapper, WebClient webClient) {
        this.objectMapper = objectMapper;
        this.webClient = webClient;
    }


    @Override
    public void postLocomotiveInfo() {
        try {
            String[] nameList = {"Zulu", "Puffing Billy", "Flying Scotsman"};
            String[] dimensionList = {"10x15", "10x15", "10x20"};
            String[] statusList = {"Poor", "Good", "Excelent"};

            // create object locomotive
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Jakarta"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
            String formattedDateTime = zonedDateTime.format(formatter);

            Random randomNumber = new Random();
            LocomotiveInfo locomotiveInfo = LocomotiveInfo.builder()
                    .locoCode(UUID.randomUUID().toString())
                    .locoName(nameList[randomNumber.nextInt(nameList.length)])
                    .locoDimension(dimensionList[randomNumber.nextInt(dimensionList.length)])
                    .locoStatus(statusList[randomNumber.nextInt(statusList.length)])
                    .createdAt(formattedDateTime)
                    .build();

            // call api node js
            // webclient
            KafkaResponse kafkaResponse = webClient.post()
                    .uri("http://localhost:8081/api/locomotive/send")
                    .bodyValue(locomotiveInfo)
                    .retrieve()
                    .bodyToMono(KafkaResponse.class)
                    .block();

            // logging
            try {
                String logLocomotiveInfo = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(locomotiveInfo);
                String logKafkaResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(kafkaResponse);
                log.info("\nKafka Response :\n {}", logKafkaResponse);
                log.info("\nLocomotive Info :\n {}", logLocomotiveInfo);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            log.error("Failed to post Locomotive Info", e);
            throw new RuntimeException(e);
        }
    }
}
