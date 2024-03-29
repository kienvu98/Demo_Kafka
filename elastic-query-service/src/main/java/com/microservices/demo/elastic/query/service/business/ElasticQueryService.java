package com.microservices.demo.elastic.query.service.business;

import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryServiceAnalyticsResponseModel;

import java.util.List;

public interface ElasticQueryService {
    ElasticQueryServiceResponseModel getDocumentById(String id);
    ElasticQueryServiceAnalyticsResponseModel getDocumentsByText(String text, String accessToken);
    List<ElasticQueryServiceResponseModel> getAllDocuments();
}
