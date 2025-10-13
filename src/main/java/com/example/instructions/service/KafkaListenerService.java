package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.InputTrade;
import com.example.instructions.util.TradeTransformer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KafkaListenerService {
    private final Map<String, CanonicalTrade> messageStore = new ConcurrentHashMap<>();

    @KafkaListener(topics = "${kafka.topic.inbound}" ,
            groupId = "group_id",
            containerFactory = "tradeListener")

    public void consume(InputTrade inputTrade)
    {
        System.out.println("Received input trade " + inputTrade);

        CanonicalTrade canonicalTrade = TradeTransformer.toCanonicalTrade(inputTrade);
        System.out.println("Convert to canonical trade = " + canonicalTrade.toString());

        String id = UUID.randomUUID().toString(); // generate unique key
        messageStore.put(id, canonicalTrade);
    }

    public Map<String, CanonicalTrade> getMessages() {
        return messageStore;
    }
}
