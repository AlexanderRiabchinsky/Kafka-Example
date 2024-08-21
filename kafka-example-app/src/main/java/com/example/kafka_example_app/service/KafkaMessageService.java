package com.example.kafka_example_app.service;

import com.example.kafka_example_app.model.KafkaMessage;
import com.example.kafka_example_app.model.Message;
import com.example.kafka_example_app.repository.MessagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaMessageService {
    private final MessagesRepository messagesRepository;
    private final List<KafkaMessage> messages = new ArrayList<>();

    public void add(KafkaMessage message){
        messages.add(message);
    }
    public Optional<KafkaMessage> getByCode(String code){
        KafkaMessage kafkaMessage = messages.stream().filter(it->it.getCode().equals(code)).findFirst().get();
        Message message = new Message();
        message.setCode(kafkaMessage.getCode());
        message.setLabel(kafkaMessage.getLabel());
        messagesRepository.save(message);
        return messages.stream().filter(it->it.getCode().equals(code)).findFirst();
    }
}
