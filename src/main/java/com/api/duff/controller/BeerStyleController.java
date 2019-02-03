package com.api.duff.controller;

import com.api.duff.dto.BeerStyleDto;
import com.api.duff.service.BeerStyleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
public class BeerStyleController {

    private final BeerStyleService service;

    public BeerStyleController(BeerStyleService service) {
        this.service = service;
    }

    @ResponseStatus(OK)
    @GetMapping("/beer-styles")
    public Flux<BeerStyleDto> getAll() {
        return service.getAll()
                .map(BeerStyleDto::beerStyleDtoOf)
                .doOnError(e -> log.error("error mapping beer style dto, message={}", e.getMessage()));
    }

    @ResponseStatus(OK)
    @GetMapping("/beer-styles/{id}")
    public Mono<BeerStyleDto> getById(@PathVariable String id) {
        return service.getById(id)
                .map(BeerStyleDto::beerStyleDtoOf)
                .doOnError(e -> log.error("error mapping beer style dto, message={}", e.getMessage()));
    }

    @ResponseStatus(CREATED)
    @PostMapping("/beer-styles")
    public Mono<BeerStyleDto> create(@RequestBody BeerStyleDto beerStyleDto) {
        var beerStyle = beerStyleDto.toBeerStyle();
        return service.create(beerStyle)
                .map(BeerStyleDto::beerStyleDtoOf)
                .doOnError(e -> log.error("error mapping beer style dto, message={}", e.getMessage()));
    }

    @ResponseStatus(OK)
    @PutMapping(value = "/beer-styles/{id}")
    public Mono<BeerStyleDto> updateById(@PathVariable String id, @RequestBody BeerStyleDto beerStyleDto) {
        var beerStyle = beerStyleDto.toBeerStyle();
        return service.updateById(id, beerStyle)
                .map(BeerStyleDto::beerStyleDtoOf)
                .doOnError(e -> log.error("error mapping beer style dto, message={}", e.getMessage()));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/beer-styles/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        return service.deleteById(id);
    }

}
