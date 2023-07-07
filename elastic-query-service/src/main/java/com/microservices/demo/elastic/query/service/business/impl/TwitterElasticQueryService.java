package com.microservices.demo.elastic.query.service.business.impl;

import com.microservices.demo.config.ElasticQuerySericeConfigData;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.QueryType;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.common.exception.ElasticQueryServiceException;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.common.transformer.ElasticToResponseModelTransformer;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryServiceAnalyticsResponseModel;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryServiceWordCountResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;
    private final ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler;
    private final ElasticQuerySericeConfigData elasticQuerySericeConfigData;
    private final WebClient.Builder weBuilder;

    public TwitterElasticQueryService(ElasticToResponseModelTransformer transformer,
                                      ElasticQueryClient<TwitterIndexModel> queryClient,
                                      ElasticQueryServiceResponseModelAssembler assembler,
                                      ElasticQuerySericeConfigData elasticQuerySericeConfigData,
                                      WebClient.Builder weBuilder) {
        this.elasticToResponseModelTransformer = transformer;
        this.elasticQueryClient = queryClient;
        this.elasticQueryServiceResponseModelAssembler = assembler;
        this.elasticQuerySericeConfigData = elasticQuerySericeConfigData;
        this.weBuilder = weBuilder;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        LOG.info("Querying elasticsearch by id {}", id);
        ElasticQueryServiceResponseModel result = elasticQueryServiceResponseModelAssembler.toModel(elasticQueryClient.getIndexModelById(id));
        return result;
    }

    @Override
    public ElasticQueryServiceAnalyticsResponseModel getDocumentsByText(String text, String accessToken) {
        LOG.info("Querying elasticsearch by text {}", text);
        List<ElasticQueryServiceResponseModel> result =
                elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getIndexModelByText(text));
        return ElasticQueryServiceAnalyticsResponseModel.builder()
                .queryResponseModels(result)
                .wordCount(getWordCount(text, accessToken)).build();
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        LOG.info("Querying all documents elasticsearch ");
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getAllIndexModels());
    }

    private Long getWordCount(String text, String accessToken) {
        if (QueryType.KAFKA_STATE_STORE.getType().equals(elasticQuerySericeConfigData.getWebClient().getQueryType())) {
            return getFromKafkaStateStore(text, accessToken).getWordCount();
        } else if (QueryType.ANALYTICS_DATABASE.getType().equals(elasticQuerySericeConfigData.getWebClient().getQueryType())){
            return getFromAnalyticsDatabase(text, accessToken).getWordCount();
        }
        return 0L;
    }

    private ElasticQueryServiceWordCountResponseModel getFromAnalyticsDatabase(String text, String accessToken) {
        ElasticQuerySericeConfigData.Query query = elasticQuerySericeConfigData.getQueryFromAnalyticsDatabase();
        return retrieverResoponesModel(text,accessToken,query);
    }

    private ElasticQueryServiceWordCountResponseModel getFromKafkaStateStore(String text, String accessToken) {
        ElasticQuerySericeConfigData.Query query = elasticQuerySericeConfigData.getQueryFromKafkaStateStore();
        return retrieverResoponesModel(text, accessToken, query);
    }

    private ElasticQueryServiceWordCountResponseModel retrieverResoponesModel(String text, String accessToken, ElasticQuerySericeConfigData.Query query) {
        return weBuilder
                .build()
                .method(HttpMethod.valueOf(query.getMethod()))
                .uri(query.getUri(), uriBuilder -> uriBuilder.build(text))
                .headers(h -> h.setBasicAuth(accessToken))
                .accept(MediaType.valueOf(query.getAccept()))
                .retrieve()
                .onStatus(
                        s -> s.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not authenticated")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        clientResponse -> Mono.just(new
                                ElasticQueryServiceException(clientResponse.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase())))
                .bodyToMono(ElasticQueryServiceWordCountResponseModel.class)
                .log()
                .block();
    }
}
