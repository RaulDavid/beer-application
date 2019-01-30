package com.api.duff.repository;

import com.api.duff.domain.BeerStyle;
import reactor.core.publisher.Mono;

public interface CustomBeerStyleRepository {

    public Mono<BeerStyle> findByTemperature(int temperature);
}
