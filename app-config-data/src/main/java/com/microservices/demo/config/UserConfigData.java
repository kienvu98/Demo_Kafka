package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "user-config")
@Configuration
public class UserConfigData {
    private String username;
    private String password;
    private String[] roles;
}
