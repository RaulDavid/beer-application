package com.api.duff.repository;

import com.api.duff.DuffApplicationTests;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.api.duff.domain.BeerStyle.beerStyleOf;
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
        var weissbier = beerStyleOf("Weissbier", 10, -2);
        var pilsen = beerStyleOf("Pilsens", 10, -4);
        var weizenbier = beerStyleOf("Weizenbier", 10, -6);
        var redAle = beerStyleOf("Red ale", 10, -8);
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
                beerStyleOf("Pilsens", 4, -2),
                beerStyleOf("Weizenbier", 6, -4));

        repository.saveAll(beers).collectList().block();
        int temperature = 2;
        var pilsenBeerStyle = beers.get(0);

        var result = repository.findByTemperature(temperature).block();
        assertEquals(pilsenBeerStyle, result);
    }
}