package com.example.instructions.config;

import com.example.instructions.batch.KafkaTradeReader;
import com.example.instructions.batch.KafkaTradeWriter;
import com.example.instructions.batch.TradeItemProcessor;
import com.example.instructions.model.InputTrade;
import com.example.instructions.model.PlatformTrade;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public ItemReader<InputTrade> reader(ConsumerFactory<String, String> cf) {
        return new KafkaTradeReader(cf, "${kafka.topic.inbound}");
    }

    @Bean
    public TradeItemProcessor processor() {
        return new TradeItemProcessor();
    }

    @Bean
    public KafkaTradeWriter writer(KafkaTemplate<String, PlatformTrade> kafkaTemplate) {
        return new KafkaTradeWriter(kafkaTemplate);
    }

    @Bean
    public Job kafkaJob(JobRepository jobRepository,
                        PlatformTransactionManager transactionManager,
                        ItemReader<InputTrade> reader,
                        TradeItemProcessor processor,
                        KafkaTradeWriter writer) {

        Step step = new StepBuilder("kafka-step", jobRepository)
                .<InputTrade, PlatformTrade>chunk(5, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

        return new JobBuilder("kafka-job", jobRepository)
                .start(step)
                .build();
    }
}
