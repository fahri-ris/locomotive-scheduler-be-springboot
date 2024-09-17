package com.ris.locomotivescheduler.repository;

import com.ris.locomotivescheduler.model.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryRepository extends JpaRepository<Summary, Integer> {
}