package com.pipeline.event_pipeline.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aggregated_stat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "window_start")
    private Instant windowStart;

    @Column(name = "count")
    private Long count;

    @Column(name = "avg_value")
    private Double avgValue;
}