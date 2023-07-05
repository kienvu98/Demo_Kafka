package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "elastic-query-service")
public class ElasticQuerySericeConfigData {
    private String version;
    private Long backPressureDelayMs;
    private String customAudience;
}
