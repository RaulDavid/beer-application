package com.api.duff.controller;

import com.api.duff.domain.BeerStyle;
import com.api.duff.service.BeerStyleService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BeerStyleController {

    private final BeerStyleService service;

    public BeerStyleController(BeerStyleService service) {
        this.service = service;
    }

    @GetMapping("/beer-styles")
    public Flux<BeerStyle> getAllBeers() {
        return service.getAll();
    }

    @GetMapping(value = "/beer-styles", params = "temperature")
    public Mono<BeerStyle> getByTemperature(@RequestParam int temperature) {
        return service.findByTemperature(temperature);
    }

    @PostMapping("/beer-styles")
    public Mono<BeerStyle> createBeerStyle(@RequestBody BeerStyle beerStyle) {
        return service.create(beerStyle);
    }

    @PutMapping(value = "/beer-styles/{id}")
    public Mono<BeerStyle> updateBeerStyleById(@PathVariable String id, @RequestBody BeerStyle beerStyle) {
        return service.updateById(id, beerStyle);
    }

    @DeleteMapping("/beer-styles/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        return service.deleteById(id);
    }

}
