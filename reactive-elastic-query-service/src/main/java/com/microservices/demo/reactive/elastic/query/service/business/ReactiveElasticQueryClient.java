package com.microservices.demo.reactive.elastic.query.service.business;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import reactor.core.publisher.Flux;

public interface ReactiveElasticQueryClient<T extends TwitterIndexModel> {
    Flux<TwitterIndexModel> getIndexModelByText(String text);
}
