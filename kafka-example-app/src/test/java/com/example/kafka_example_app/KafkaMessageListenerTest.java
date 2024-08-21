package com.example.kafka_example_app;

import com.example.kafka_example_app.model.KafkaMessage;
import com.example.kafka_example_app.service.KafkaMessageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
public class KafkaMessageListenerTest {
    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
    );
    @DynamicPropertySource
    static void registryKafkaProperties(DynamicPropertyRegistry registry){
        registry.add("spring.kafka.bbootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaMessageService kafkaMessageService;

    @Autowired
    private KafkaTemplate<String,KafkaMessage> kafkaTemplate;

    @Value("${app.kafka.kafkaMessageTopic}")
    private String topicName;

    @Test
    public void whenSendKafkaMessage_thenHandleMessageByListener(){
        KafkaMessage event = new KafkaMessage();
        event.setCode("1");
        event.setLabel("Message from Kafka Test");
        String key = UUID.randomUUID().toString();

        kafkaTemplate.send(topicName,key,event);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(100, TimeUnit.SECONDS)
                .untilAsserted(()->{
                    Optional<KafkaMessage>mayBeKafkaMessage = kafkaMessageService.getByCode("1");
                    assertThat(mayBeKafkaMessage).isPresent();

                    KafkaMessage kafkaMessage = mayBeKafkaMessage.get();

                    assertThat(kafkaMessage.getLabel()).isEqualTo("Message from Kafka Test");
                    assertThat(kafkaMessage.getCode()).isEqualTo("1");
                });
    }
}
