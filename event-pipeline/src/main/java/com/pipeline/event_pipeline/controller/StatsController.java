package com.pipeline.event_pipeline.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pipeline.event_pipeline.model.AggregatedStat;
import com.pipeline.event_pipeline.repository.StatsRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsRepository statsRepository;

    @GetMapping
    public List<AggregatedStat> getAllStats() {
        return statsRepository.findAllByOrderByWindowStartDesc();
    }

    @GetMapping("/{eventType}")
    public ResponseEntity<AggregatedStat> getStatsByType(@PathVariable String eventType) {
        return statsRepository.findTopByEventTypeOrderByWindowStartDesc(eventType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        Instant cutoff = Instant.now().minusSeconds(60);
        List<AggregatedStat> recentStats = statsRepository.findByWindowStartAfter(cutoff);

        long totalEvents = recentStats.stream().mapToLong(AggregatedStat::getCount).sum();

        Map<String, Long> countsByType = new HashMap<>();
        recentStats.forEach(stat ->
            countsByType.merge(stat.getEventType(), stat.getCount(), Long::sum)
        );

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalEvents", totalEvents);
        summary.put("countsByType", countsByType);
        summary.put("windows", recentStats);
        summary.put("periodSeconds", 60);

        return ResponseEntity.ok(summary);
    }
}