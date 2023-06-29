package com.microservices.demo.reactive.elastic.query.service.repository;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ElasticQueryRepository extends ReactiveCrudRepository<TwitterIndexModel, String> {

    // su dung mo rong ReactiveCrudRepository
    Flux<TwitterIndexModel> findByText(String text);

}
