package com.microservices.demo.kafka.to.elastic.service.consumer.impl;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.kafka.to.elastic.service.consumer.KafkaConsumer;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitterKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaConsumer.class);
    private final KafkaListenerEndpointRegistrar kafkaListenerEndpointRegistrar;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;

    public TwitterKafkaConsumer(KafkaListenerEndpointRegistrar listenerEndpointRegistrar,
                                KafkaAdminClient adminClient,
                                KafkaConfigData configData) {
        this.kafkaListenerEndpointRegistrar = listenerEndpointRegistrar;
        this.kafkaAdminClient = adminClient;
        this.kafkaConfigData = configData;
    }

    // gửi tín nhắn đến các consumer
    @Override
    @KafkaListener(id = "twitterTopicListener", topics = "kafka-config.topic-name")
    public void receive(@Payload List<TwitterAvroModel> message,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        LOG.info("{} number of message received with keys {}, partitions {} and offsets {}, sending it to elastic: Thread id {}",
                message.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                Thread.currentThread().getId());
    }
}
