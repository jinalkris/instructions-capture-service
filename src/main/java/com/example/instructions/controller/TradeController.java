package com.example.instructions.controller;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;
import com.example.instructions.service.KafkaListenerService;
import com.example.instructions.service.KafkaPublisher;
import com.example.instructions.util.TradeTransformer;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/instructions")
public class TradeController {

    private final KafkaListenerService  kafkaListenerService;
    private final KafkaPublisher producer;

    public TradeController(KafkaListenerService kafkaListenerService, KafkaPublisher producer) {
        this.kafkaListenerService = kafkaListenerService;
        this.producer = producer;
    }

    @GetMapping("/process-messages")
    public String processAndPublish() {
        Map<String, CanonicalTrade> canonicalTradeMap = kafkaListenerService.getMessages();

        if(!canonicalTradeMap.isEmpty()) {
            System.out.println("No message available in Kafka.");
            return "No message available in Kafka.";
        }

        canonicalTradeMap.values().forEach(canonicalTrade -> {
            PlatformTrade platformTrade = TradeTransformer.toPlatformTrade(canonicalTrade);
            producer.sendMessage(platformTrade);
        });
        return "success";
    }
}
