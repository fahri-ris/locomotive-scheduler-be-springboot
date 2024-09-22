package com.ris.locomotivescheduler.repository;

import com.ris.locomotivescheduler.model.LocomotiveInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public interface LocomotiveInfoRepository extends MongoRepository<LocomotiveInfo, String> {
    @Query("select count(s) from Summary s where s.status = ?1 and s.createdAt >= ?2")
    int countByLocoStatusIgnoreCaseAndCreatedAtAfter(String status, String createdAt);
}