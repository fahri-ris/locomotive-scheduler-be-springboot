package com.ris.locomotivescheduler.repository;

import com.ris.locomotivescheduler.model.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SummaryRepository extends JpaRepository<Summary, Integer> {
    @Query("select sum(s.total) from Summary s where lower(s.status) = lower(?1) and substring(s.createdAt, 1 ,4) = ?2")
    Integer sumTotalByStatusAndYear(String status, String year);

    @Query("select sum(s.total) from Summary s where lower(s.status) = lower(?1) and substring(s.createdAt, 1 ,7) = ?2")
    Integer sumTotalByStatusAndYearMonth(String status, String year);
}