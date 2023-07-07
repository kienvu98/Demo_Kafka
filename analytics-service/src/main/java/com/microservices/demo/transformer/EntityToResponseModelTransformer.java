package com.microservices.demo.transformer;

import com.microservices.demo.dataccess.entity.AnalyticsEntity;
import com.microservices.demo.model.AnalyticsResponseModel;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EntityToResponseModelTransformer {

    public Optional<AnalyticsResponseModel> getResponseModel(AnalyticsEntity analyticsEntity) {
        if (analyticsEntity == null) {
            return Optional.empty();
        }
        else {
            return Optional.ofNullable(
                    AnalyticsResponseModel.builder()
                            .id(analyticsEntity.getId())
                            .word(analyticsEntity.getWord())
                            .wordCount(analyticsEntity.getWordCount())
                            .build());
        }
    }
}
