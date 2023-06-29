package com.microservices.demo.reactive.elastic.query.service.business.impl;

import com.microservices.demo.config.ElasticQueryConfigData;
import com.microservices.demo.config.ElasticQuerySericeConfigData;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.reactive.elastic.query.service.business.ReactiveElasticQueryClient;
import com.microservices.demo.reactive.elastic.query.service.repository.ElasticQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class TwitterReactiveElasticQueryClient implements ReactiveElasticQueryClient<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterReactiveElasticQueryClient.class);

    private final ElasticQueryRepository elasticQueryRepository;

    private final ElasticQuerySericeConfigData elasticQuerySericeConfigData;

    public TwitterReactiveElasticQueryClient(ElasticQueryRepository queryRepository,
                                             ElasticQuerySericeConfigData configData) {
        this.elasticQueryRepository = queryRepository;
        this.elasticQuerySericeConfigData = configData;
    }

    @Override
    public Flux<TwitterIndexModel> getIndexModelByText(String text) {
        LOG.info("Getting data from elasticSearch for text {}",text);
        Flux<TwitterIndexModel> result = elasticQueryRepository.findByText(text)
                .delayElements(Duration.ofMillis(elasticQuerySericeConfigData.getBackPressureDelayMs()));
        return result;
    }
}
