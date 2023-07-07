package com.microservices.demo.kafka.consumer.api;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

public interface KafkaConsumer<V extends SpecificRecordBase> {

    void receive(List<V> messages, List<Long> keys, List<Integer> partition, List<Long> offsets);
}
