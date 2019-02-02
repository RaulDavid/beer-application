package com.api.duff.repository;

import com.api.duff.DuffApplicationTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.api.duff.domain.BeerStyle.beerStyleOf;
import static org.junit.jupiter.api.Assertions.assertEquals;


class BeerStyleRepositoryIT extends DuffApplicationTests {

    @Autowired
    private BeerStyleRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    @DisplayName("should consider closer avg temperature")
    void closerAvgTemperature() {
        //given
        var weissbier = beerStyleOf("Weissbier", 10, -2);
        var pilsen = beerStyleOf("Pilsens", 10, -4);
        var weizenbier = beerStyleOf("Weizenbier", 10, -6);
        var redAle = beerStyleOf("Red ale", 10, -8);
        var beers = List.of(weissbier, pilsen, weizenbier, redAle);

        repository.saveAll(beers).collectList().block();

        //when
        var result = repository.findByTemperature(1).block();
        //then
        assertEquals(redAle, result);

        //when
        result = repository.findByTemperature(2).block();
        //then
        assertEquals(weizenbier, result);

        //when
        result = repository.findByTemperature(3).block();
        //then
        assertEquals(pilsen, result);

        //when
        result = repository.findByTemperature(4).block();
        //then
        assertEquals(weissbier, result);
    }

    @Test
    @DisplayName("should consider closer avg temperature name when two beer styles have same avg temperature")
    void considerName() {
        //given
        var beers = List.of(
                beerStyleOf("Pilsens", 4, -2),
                beerStyleOf("Weizenbier", 6, -4));

        repository.saveAll(beers).collectList().block();
        int temperature = 2;
        var pilsenBeerStyle = beers.get(0);

        //when
        var result = repository.findByTemperature(temperature).block();

        //then
        assertEquals(pilsenBeerStyle, result);
    }
}