package com.microservices.demo.kafka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.microservices.demo")
public class KafkaStreamsServiceApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamsServiceApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
