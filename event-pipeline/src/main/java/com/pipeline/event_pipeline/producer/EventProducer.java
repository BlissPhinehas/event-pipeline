package com.pipeline.event_pipeline.producer;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Random random = new Random();

    private static final String TOPIC = "events";
    private static final List<String> EVENT_TYPES = List.of("click", "purchase", "search", "view");

    @Scheduled(fixedDelay = 500)
    public void publishEvent() {
        String eventType = EVENT_TYPES.get(random.nextInt(EVENT_TYPES.size()));
        double value = Math.round(random.nextDouble() * 100 * 100.0) / 100.0;
        String userId = "user-" + random.nextInt(100);

        String payload = String.format(
            "{\"type\":\"%s\",\"userId\":\"%s\",\"value\":%.2f,\"timestamp\":\"%s\"}",
            eventType, userId, value, Instant.now().toString()
        );

        kafkaTemplate.send(TOPIC, UUID.randomUUID().toString(), payload);
        log.info("Published event: {}", payload);
    }
}