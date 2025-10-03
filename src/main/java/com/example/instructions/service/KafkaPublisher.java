package com.example.instructions.service;

import com.example.instructions.model.PlatformTrade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublisher {

    @Value("${kafka.topic.outbound}")
    private String outboundTopic;

    private final KafkaTemplate<String, PlatformTrade> kafkaTemplate;

    public KafkaPublisher(KafkaTemplate<String, PlatformTrade> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(PlatformTrade message) {
        System.out.println("📤 Sending Kafka Message: " + message);
        kafkaTemplate.send(outboundTopic, message);
    }
}
