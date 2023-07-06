package com.microservices.demo.kafka.services.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkastreamsResponseModel {
    private String word;
    private Long wordCount;
}
