package com.api.duff.service;

import com.api.duff.domain.BeerStyle;
import com.api.duff.repository.BeerStyleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.api.duff.error.Errors.beerStyleNotFound;

@Slf4j
@Service
public class BeerStyleService {

    private final BeerStyleRepository repository;

    public BeerStyleService(BeerStyleRepository repository) {
        this.repository = repository;
    }

    public Flux<BeerStyle> getAll() {
        log.info("getting all beer styles");
        return repository.findAll()
                .switchIfEmpty(beerStyleNotFound());
    }

    public Mono<BeerStyle> getById(String id) {
        log.info("getting beer style by id, id={}", id);
        return repository.findById(id)
                .switchIfEmpty(beerStyleNotFound());
    }

    public Mono<BeerStyle> create(BeerStyle beerStyle) {
        log.info("creating beer style, beerStyle={}", beerStyle);
        return repository.save(beerStyle)
                .doOnError(e -> log.error("error saving beerStyle, beerStyle={}, message={}", beerStyle, e.getMessage()));
    }

    public Mono<BeerStyle> updateById(String id, BeerStyle beerStyle) {
        beerStyle.setId(id);
        log.info("updating beer style by id={} beerStyle={}", id, beerStyle);
        return repository.findById(beerStyle.getId())
                .switchIfEmpty(beerStyleNotFound())
                .flatMap(persistedBeerStyle -> repository.save(beerStyle))
                .doOnError(e -> log.error("error updating beerStyle, id={}, beerStyle={}, message={}", id, beerStyle, e.getMessage()));
    }

    public Mono<Void> deleteById(String id) {
        log.info("deleting beer style by id={}", id);
        return repository.deleteById(id)
                .doOnError(e -> log.error("error deleting by id, message={}", e.getMessage()));
    }

    public Mono<BeerStyle> findByCloserAvgTemperature(int temperature) {
        log.info("finding beer style by temperature={}", temperature);
        return repository.findByTemperature(temperature)
                .switchIfEmpty(beerStyleNotFound())
                .doOnError(e -> log.error("error finding by temperature, message={}", e.getMessage()));
    }
}
