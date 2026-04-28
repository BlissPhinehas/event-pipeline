package com.pipeline.event_pipeline.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pipeline.event_pipeline.model.AggregatedStat;

@Repository
public interface StatsRepository extends JpaRepository<AggregatedStat, Long> {

    List<AggregatedStat> findByWindowStartAfter(Instant cutoff);

    Optional<AggregatedStat> findTopByEventTypeOrderByWindowStartDesc(String eventType);

    List<AggregatedStat> findAllByOrderByWindowStartDesc();
}