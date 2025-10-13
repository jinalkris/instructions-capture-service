package com.example.instructions.batch;

import com.example.instructions.model.PlatformTrade;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaTradeWriter implements ItemWriter<PlatformTrade> {

    @Value("${kafka.topic.outbound}")
    private String outboundTopic;

    private final KafkaTemplate<String, PlatformTrade> kafkaTemplate;

    public KafkaTradeWriter(KafkaTemplate<String, PlatformTrade> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void write(Chunk<? extends PlatformTrade> chunks) throws Exception {
        for (PlatformTrade trade : chunks) {
            kafkaTemplate.send(outboundTopic, trade);
        }
    }
}
