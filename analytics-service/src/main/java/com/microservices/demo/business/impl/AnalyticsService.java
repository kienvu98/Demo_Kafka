package com.microservices.demo.business.impl;

import com.microservices.demo.model.AnalyticsResponseModel;

import java.util.Optional;

public interface AnalyticsService {
    Optional<AnalyticsResponseModel> getWordAnalytics(String word);
}
