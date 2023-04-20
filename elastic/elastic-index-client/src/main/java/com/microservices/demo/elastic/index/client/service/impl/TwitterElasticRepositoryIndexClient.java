package com.microservices.demo.elastic.index.client.service.impl;

import com.microservices.demo.elastic.index.client.repository.TwitterElasticserachIndexRepository;
import com.microservices.demo.elastic.index.client.service.ElasticClient;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
public class TwitterElasticRepositoryIndexClient implements ElasticClient<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticIndexClient.class);

    private final TwitterElasticserachIndexRepository twitterElasticserachIndexRepository;

    public TwitterElasticRepositoryIndexClient(TwitterElasticserachIndexRepository indexRepository) {
        this.twitterElasticserachIndexRepository = indexRepository;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> reposirotyResponse = (List<TwitterIndexModel>) twitterElasticserachIndexRepository.saveAll(documents);
        List<String> ids = reposirotyResponse.stream().map(TwitterIndexModel::getId).collect(Collectors.toList());
        LOG.info("Documents indexed successfuly with type: {} and ids: {}", TwitterIndexModel.class.getName(), ids);
        return ids;
    }
}
