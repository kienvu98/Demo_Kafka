package com.microservices.demo.elastic.query.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@SpringBootApplication
@ComponentScan(basePackages = "com.microservices.demo")
public class ElasticQueryServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(ElasticQueryServiceApplication.class, args);
    }
}
