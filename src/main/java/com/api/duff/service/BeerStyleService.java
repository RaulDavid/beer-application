package com.api.duff.service;

import com.api.duff.domain.BeerStyle;
import com.api.duff.repository.BeerStyleRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BeerStyleService {

    private final BeerStyleRepository repository;

    public BeerStyleService(BeerStyleRepository repository) {
        this.repository = repository;
    }

    public Flux<BeerStyle> getAll() {
        return repository.findAll();
    }

    public Mono<BeerStyle> create(BeerStyle beerStyle) {
        return repository.save(beerStyle);
    }

    public Mono<BeerStyle> updateById(String id, BeerStyle beerStyle) {
        beerStyle.setId(id);
        return repository.save(beerStyle);
    }

    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    public Mono<BeerStyle> findByTemperature(int temperature) {
        return repository.findByTemperature(temperature);
    }
}
