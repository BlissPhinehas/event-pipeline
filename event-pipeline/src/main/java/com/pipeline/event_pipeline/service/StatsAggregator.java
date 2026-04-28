package com.pipeline.event_pipeline.service;

import java.time.Instant;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pipeline.event_pipeline.consumer.EventConsumer;
import com.pipeline.event_pipeline.model.AggregatedStat;
import com.pipeline.event_pipeline.repository.StatsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsAggregator {

    private final StatsRepository statsRepository;

    @Scheduled(fixedDelay = 5000)
    public void aggregateAndSave() {
        Instant windowStart = Instant.now();

        for (Map.Entry<String, Long> entry : EventConsumer.eventCounts.entrySet()) {
            String eventType = entry.getKey();
            Long count = entry.getValue();
            Double valueSum = EventConsumer.eventValueSums.getOrDefault(eventType, 0.0);
            Double avgValue = count > 0 ? valueSum / count : 0.0;

            AggregatedStat stat = new AggregatedStat();
            stat.setEventType(eventType);
            stat.setWindowStart(windowStart);
            stat.setCount(count);
            stat.setAvgValue(avgValue);

            statsRepository.save(stat);
            log.info("Saved stat: type={}, count={}, avg={}", eventType, count, avgValue);
        }

        EventConsumer.eventCounts.clear();
        EventConsumer.eventValueSums.clear();
    }
}