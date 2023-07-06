package com.microservices.demo.kafka.services.runner;

public interface StreamsRunner<K,V>{
    void start();
    default V getValueByKey(K key){
        return null;
    }
}
