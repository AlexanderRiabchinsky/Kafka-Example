package com.example.kafka_example_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaExampleAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaExampleAppApplication.class, args);
	}

}
