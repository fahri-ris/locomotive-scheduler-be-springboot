package com.ris.locomotivescheduler.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "summary")
@Builder
public class Summary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "total")
    private Integer total;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private String createdAt;

}