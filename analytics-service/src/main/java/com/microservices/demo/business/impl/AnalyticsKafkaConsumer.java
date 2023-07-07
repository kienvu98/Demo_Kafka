package com.microservices.demo.business.impl;


import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.dataccess.entity.AnalyticsEntity;
import com.microservices.demo.dataccess.repositoty.AnalyticsRepository;
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import com.microservices.demo.kafka.avro.model.TwitterAnalyticsAvroModel;
import com.microservices.demo.kafka.consumer.api.KafkaConsumer;
import com.microservices.demo.transformer.AvroToDbEntityModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;

public class AnalyticsKafkaConsumer implements KafkaConsumer<TwitterAnalyticsAvroModel> {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsKafkaConsumer.class);

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private final KafkaAdminClient kafkaAdminClient;

    private final KafkaConfigData kafkaConfigData;

    private final AnalyticsRepository analyticsRepository;

    private final AvroToDbEntityModelTransformer avroToDbEntityModelTransformer;

    public AnalyticsKafkaConsumer(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                                  KafkaAdminClient kafkaAdminClient,
                                  KafkaConfigData kafkaConfigData,
                                  AnalyticsRepository analyticsRepository,
                                  AvroToDbEntityModelTransformer avroToDbEntityModelTransformer) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
        this.analyticsRepository = analyticsRepository;
        this.avroToDbEntityModelTransformer = avroToDbEntityModelTransformer;
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        LOG.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
        kafkaListenerEndpointRegistry.getListenerContainer("tiwtterAnalyticsTopicListener").start();
    }


    @Override
    @KafkaListener(id = "twitterAnalyticsTopicListener", topics = "${kafka-config.topic-name}",autoStartup = "false")
    public void receive(@Payload List<TwitterAnalyticsAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Long> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partition,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        LOG.info("{} number of message recevied with keys {}, partitons {} and offsests {} sending it to database: thread is {}",
                messages.size(), keys.toString(), partition.toString(), offsets.toString(), Thread.currentThread().getId());
        List<AnalyticsEntity> analyticsEntities = avroToDbEntityModelTransformer.getEntityModel(messages);
        analyticsRepository.batchPrersist(analyticsEntities);
        LOG.info("{} number of messaged sending to database", analyticsEntities.size());
    }
}
