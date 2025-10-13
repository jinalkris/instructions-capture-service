package com.example.instructions.batch;

import com.example.instructions.model.InputTrade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.batch.item.ItemReader;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;

@Component
public class KafkaTradeReader implements ItemReader<InputTrade> {

    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaTradeReader(ConsumerFactory<String, String> consumerFactory, String topic) {
        this.consumer = (KafkaConsumer<String, String>) consumerFactory.createConsumer();
        this.consumer.subscribe(Collections.singletonList(topic));
    }


    @Override
    public InputTrade read() throws Exception {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(2));
        for (ConsumerRecord<String, String> record : records) {
            return objectMapper.readValue(record.value(), InputTrade.class);
        }
        return null; // end of data
    }
}
