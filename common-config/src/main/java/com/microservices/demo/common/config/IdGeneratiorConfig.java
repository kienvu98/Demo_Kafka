package com.microservices.demo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;

@Configuration
public class IdGeneratiorConfig {

    @Bean
    public IdGenerator idGenerator() {
        return new JdkIdGenerator();
    }
}
