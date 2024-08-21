package com.example.kafka_example_app.repository;

import com.example.kafka_example_app.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<Message,Long> {
}
