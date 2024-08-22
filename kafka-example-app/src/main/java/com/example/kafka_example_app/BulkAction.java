package com.example.kafka_example_app;

import com.example.kafka_example_app.model.KafkaMessage;
import com.example.kafka_example_app.service.KafkaMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class BulkAction {

    @Value("${app.kafka.kafkaMessageTopic}")
    private String topicName;

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    private final KafkaMessageService kafkaMessageService;

    @Scheduled(cron = "0 */3 * * * *")
    @Async
    public void producing(){
        for (int i=0;i<10000;i++){
            log.info("create "+i);
            KafkaMessage message = new KafkaMessage();
            message.setCode("Message "+(i+1));
            message.setLabel("Message Content "+(i+1));
            kafkaTemplate.send(topicName,message);
        }
    }

    @Scheduled(cron = "5 */3 * * * *")
    @Async
    public void consuming(){
        for (int i=0;i<10000;i++){
            log.info("consume "+i);
            String code = "Message "+(i+1);
            kafkaMessageService.getByCode(code);
        }
    }
}
