package com.example.instructions.controller;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.InputTrade;
import com.example.instructions.model.PlatformTrade;
import com.example.instructions.util.TradeTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@WebMvcTest(TradeControllerFileTest.class)
public class TradeControllerFileTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, CanonicalTrade> messageStore = new ConcurrentHashMap<>();

    @Test
    public void testJsonProcess() throws IOException {
        // Reading from sample input json for testing instead of kafka topic
        ClassPathResource resource = new ClassPathResource("/input.json");
        try (InputStream inputStream = resource.getInputStream()) {
            InputTrade inputTrade = mapper.readValue(inputStream, InputTrade.class);
            System.out.println("Input trade = " + inputTrade.toString());

            CanonicalTrade canonicalTrade = TradeTransformer.toCanonicalTrade(inputTrade);
            System.out.println("Convert to canonical trade = " + canonicalTrade.toString());

            String id = UUID.randomUUID().toString(); // generate unique key
            messageStore.put(id, canonicalTrade);
        }

        messageStore.values().forEach(canonicalTrade -> {
            PlatformTrade platformTrade = TradeTransformer.toPlatformTrade(canonicalTrade);
            System.out.println("Convert to Platform trade = " + platformTrade.toString());
//            producer.sendMessage(platformTrade);
        });

    }
}
