package com.microservices.demo.business.impl;

import com.microservices.demo.dataccess.repositoty.AnalyticsRepository;
import com.microservices.demo.model.AnalyticsResponseModel;
import com.microservices.demo.transformer.EntityToResponseModelTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class TwitterAnalyticsService implements AnalyticsService{

    private final AnalyticsRepository analyticsRepository;

    private final EntityToResponseModelTransformer entityToResponseModelTransformer;

    public TwitterAnalyticsService(AnalyticsRepository analyticsRepository, EntityToResponseModelTransformer entityToResponseModelTransformer) {
        this.analyticsRepository = analyticsRepository;
        this.entityToResponseModelTransformer = entityToResponseModelTransformer;
    }

    @Override
    public Optional<AnalyticsResponseModel> getWordAnalytics(String word) {
        return entityToResponseModelTransformer.getResponseModel(
                analyticsRepository.getAnalyticsEntityByWord(word, PageRequest.of(0,1))
                        .stream().findFirst().orElse(null));
    }
}
