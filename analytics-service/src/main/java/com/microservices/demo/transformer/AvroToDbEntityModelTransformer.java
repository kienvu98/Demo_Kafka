package com.microservices.demo.transformer;

import com.microservices.demo.dataccess.entity.AnalyticsEntity;
import com.microservices.demo.kafka.avro.model.TwitterAnalyticsAvroModel;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class AvroToDbEntityModelTransformer {

    private final IdGenerator idGenerator;

    public AvroToDbEntityModelTransformer(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public List<AnalyticsEntity> getEntityModel(List<TwitterAnalyticsAvroModel> avroModels) {
        return avroModels.stream()
                .map(avroModel -> new AnalyticsEntity(
                        idGenerator.generateId(), avroModel.getWord(), avroModel.getWordCount(),
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(avroModel.getCreatedAt()), ZoneOffset.UTC)))
                .collect(toList());
    }
}
