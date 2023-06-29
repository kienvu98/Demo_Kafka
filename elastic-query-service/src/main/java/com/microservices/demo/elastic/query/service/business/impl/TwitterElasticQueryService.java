package com.microservices.demo.elastic.query.service.business.impl;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.common.transformer.ElasticToResponseModelTransformer;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;
    private final ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler;

    public TwitterElasticQueryService(ElasticToResponseModelTransformer transformer,
                                      ElasticQueryClient<TwitterIndexModel> queryClient,
                                      ElasticQueryServiceResponseModelAssembler assembler) {
        this.elasticToResponseModelTransformer = transformer;
        this.elasticQueryClient = queryClient;
        this.elasticQueryServiceResponseModelAssembler = assembler;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        LOG.info("Querying elasticsearch by id {}", id);
        ElasticQueryServiceResponseModel result = elasticQueryServiceResponseModelAssembler.toModel(elasticQueryClient.getIndexModelById(id));
        return result;
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentsByText(String text) {
        LOG.info("Querying elasticsearch by text {}", text);
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getIndexModelByText(text));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        LOG.info("Querying all documents elasticsearch ");
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getAllIndexModels());
    }
}
