package com.ris.locomotivescheduler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ris.locomotivescheduler.dto.SummaryDataResponse;
import com.ris.locomotivescheduler.dto.SummaryResponse;
import com.ris.locomotivescheduler.model.Summary;
import com.ris.locomotivescheduler.repository.LocomotiveInfoRepository;
import com.ris.locomotivescheduler.repository.SummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Slf4j
@Service
public class SummaryServiceImpl implements SummaryService {
    private final SummaryRepository summaryRepository;
    private final LocomotiveInfoRepository locomotiveInfoRepository;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    @Autowired
    public SummaryServiceImpl(SummaryRepository summaryRepository, LocomotiveInfoRepository locomotiveInfoRepository, ObjectMapper objectMapper, WebClient webClient) {
        this.locomotiveInfoRepository = locomotiveInfoRepository;
        this.summaryRepository = summaryRepository;
        this.objectMapper = objectMapper;
        this.webClient = webClient;
    }

    @Value("${telegram.chat.id}")
    String chatId;

    @Value("${telegram.bot.token}")
    String botToken;

    @Override
    public SummaryResponse getAllSummary() {
        // total status last year
        String[] statusList = {"Poor", "Good", "Excelent"};

        Calendar calendar = Calendar.getInstance();
        String yearNow = String.valueOf(calendar.get(Calendar.YEAR));
        Integer monthNow = calendar.get(Calendar.MONTH);

        Integer totalStatusPoor = summaryRepository.sumTotalByStatusAndYear(statusList[0], yearNow);
        Integer totalStatusGood = summaryRepository.sumTotalByStatusAndYear(statusList[1], yearNow);
        Integer totalStatusExcelent = summaryRepository.sumTotalByStatusAndYear(statusList[2], yearNow);

        // status every month
        List<SummaryDataResponse> listDataset = new ArrayList<>();
        for(int i = 0; i < 12; i++){
            String monthCheck = String.format("%02d", i + 1);
            Month month = Month.of(i + 1);

            String monthName = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

            Integer totalStatusPoorMonth = summaryRepository.sumTotalByStatusAndYearMonth(statusList[0], yearNow + "-" + monthCheck);
            Integer totalStatusGoodMonth = summaryRepository.sumTotalByStatusAndYearMonth(statusList[1], yearNow + "-" + monthCheck);
            Integer totalStatusExcelentMonth = summaryRepository.sumTotalByStatusAndYearMonth(statusList[2], yearNow + "-" + monthCheck);

            SummaryDataResponse summaryDataResponse = toSummaryDataResponse(totalStatusPoorMonth, totalStatusGoodMonth, totalStatusExcelentMonth, monthName);
            listDataset.add(summaryDataResponse);
        }

        // build response
        SummaryResponse response = SummaryResponse.builder()
                .dataset(listDataset)
                .totalPoor(totalStatusPoor)
                .totalGood(totalStatusGood)
                .totalExcelent(totalStatusExcelent)
                .build();
        return response;
    }

    public SummaryDataResponse toSummaryDataResponse(Integer statusPoor, Integer statusGood, Integer statusExcelent, String monthName) {
        return SummaryDataResponse.builder()
                .poor(statusPoor)
                .good(statusGood)
                .excelent(statusExcelent)
                .month(monthName)
                .build();
    }

    @Override
    public void getLocoSummary() {
        try{
            String[] statusList = {"Poor", "Good", "Excelent"};
            List<Summary> listSummary = new ArrayList<>();

            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Jakarta"));
            ZonedDateTime minusOneHour = zonedDateTime.minusHours(1);

            DateTimeFormatter formatterResponse = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter formatterIso = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            DateTimeFormatter formatterCheck = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // get data by status count from mongodb
            // save summary report to postgres
            int summaryCount;
            String dateMinusOneHour = minusOneHour.format(formatterIso).substring(0, 19) + "+07:00";
            for(int i = 1; i <= statusList.length; i++) {
                summaryCount = locomotiveInfoRepository.countByLocoStatusIgnoreCaseAndCreatedAtAfter(statusList[i - 1], dateMinusOneHour);

                Summary summary = Summary.builder()
                        .status(statusList[i - 1])
                        .total(summaryCount)
                        .createdAt(zonedDateTime.format(formatterResponse))
                        .build();
                listSummary.add(summary);
            }
            List<Summary> savedSummaries = summaryRepository.saveAll(listSummary);

            // logging summary
            try {
                String logLocomotiveInfo = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(savedSummaries);
                log.info("Summary Locomotive data successfully posted");
                log.info("Summaries : {}", logLocomotiveInfo);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            // send summary to telegram using webclient
            String messageTele = "\uD83D\uDD14 Reminder: New locomotive data has just been added." +
                    "\nPlease check and review the latest update for more details.\n\n" +
                    zonedDateTime.format(formatterResponse);

            webClient.get()
                    .uri("https://api.telegram.org/bot" + botToken +"/sendMessage?chat_id=" + chatId + "&text=" + messageTele)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

             log.info("Send message to telegram successfully");
        } catch (Exception e) {
            log.info("Failed to get & post summary", e);
            throw new RuntimeException(e);
        }

    }
}
