package com.api.duff.repository;

import com.api.duff.DuffApplicationTests;
import com.api.duff.domain.BeerStyle;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class BeerStyleRepositoryIT extends DuffApplicationTests {

    @Autowired
    private BeerStyleRepository repository;

    @After
    public void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    public void findByTemperatureShouldConsiderCloserAvgTemperature() {
        var weissbier = BeerStyle.of("Weissbier", 10, -2);
        var pilsen = BeerStyle.of("Pilsens", 10, -4);
        var weizenbier = BeerStyle.of("Weizenbier", 10, -6);
        var redAle = BeerStyle.of("Red ale", 10, -8);
        var beers = List.of(weissbier, pilsen, weizenbier, redAle);

        repository.saveAll(beers).collectList().block();

        var result = repository.findByTemperature(1).block();
        assertEquals(redAle, result);

        result = repository.findByTemperature(2).block();
        assertEquals(weizenbier, result);

        result = repository.findByTemperature(3).block();
        assertEquals(pilsen, result);

        result = repository.findByTemperature(4).block();
        assertEquals(weissbier, result);
    }

    @Test
    public void findByTemperatureShouldConsiderNameWhenTwoBeerStylesHaveSameAvgTemperature() {
        var beers = List.of(
                BeerStyle.of("Pilsens", 4, -2),
                BeerStyle.of("Weizenbier", 6, -4));

        repository.saveAll(beers).collectList().block();
        int temperature = 2;
        var pilsenBeerStyle = beers.get(0);

        var result = repository.findByTemperature(temperature).block();
        assertEquals(pilsenBeerStyle, result);
    }
}