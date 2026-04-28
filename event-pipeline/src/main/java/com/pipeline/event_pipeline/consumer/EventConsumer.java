package com.pipeline.event_pipeline.consumer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final ObjectMapper objectMapper;

    public static final ConcurrentMap<String, Long> eventCounts = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, Double> eventValueSums = new ConcurrentHashMap<>();

    @KafkaListener(topics = "events", groupId = "event-pipeline-group")
    public void consume(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            String type = node.get("type").asText();
            double value = node.get("value").asDouble();

            eventCounts.merge(type, 1L, Long::sum);
            eventValueSums.merge(type, value, Double::sum);

            log.info("Consumed event: type={}, value={}", type, value);
        } catch (Exception e) {
            log.error("Failed to process message: {}", message, e);
        }
    }
}