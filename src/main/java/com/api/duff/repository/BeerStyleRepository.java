package com.api.duff.repository;

import com.api.duff.domain.BeerStyle;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BeerStyleRepository extends ReactiveMongoRepository<BeerStyle, String>, CustomBeerStyleRepository {

    Mono<BeerStyle> findByName(String name);
}
