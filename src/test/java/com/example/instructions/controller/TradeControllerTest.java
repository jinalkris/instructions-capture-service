package com.example.instructions.controller;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;
import com.example.instructions.service.KafkaListenerService;
import com.example.instructions.service.KafkaPublisher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@WebMvcTest(TradeController.class)
class TradeControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private KafkaListenerService kafkaListenerService;

        @MockBean
        private KafkaPublisher producer;

        @Test
        void testProcessAndPublish() throws Exception {
            // Arrange - mock kafka messages
            CanonicalTrade canonicalTrade = new CanonicalTrade("ACCT123", "acct1234","abc123", "B",10000, "2025-10-02T00:00:00z");
            Map<String, CanonicalTrade> tradeMap = new HashMap<>();
            tradeMap.put("1", canonicalTrade);

            when(kafkaListenerService.getMessages()).thenReturn(tradeMap);

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.post("/process-messages"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("success"));

            // Verify producer was called with transformed PlatformTrade
            ArgumentCaptor<PlatformTrade> captor = ArgumentCaptor.forClass(PlatformTrade.class);
            verify(producer, times(1)).sendMessage(captor.capture());

            PlatformTrade sentTrade = captor.getValue();
            assert sentTrade.platform_id().equals("ACCT123");  // Example assertion
        }
}