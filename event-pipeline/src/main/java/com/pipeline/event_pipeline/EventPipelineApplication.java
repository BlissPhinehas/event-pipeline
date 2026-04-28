package com.pipeline.event_pipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventPipelineApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventPipelineApplication.class, args);
	}

}
