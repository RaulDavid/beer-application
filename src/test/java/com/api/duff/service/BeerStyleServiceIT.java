package com.api.duff.service;

import com.api.duff.DuffApplicationTests;
import com.api.duff.domain.BeerStyle;
import com.api.duff.exception.NotFoundException;
import com.api.duff.repository.BeerStyleRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.api.duff.domain.BeerStyle.beerStyleOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BeerStyleServiceIT extends DuffApplicationTests {

    @Autowired
    private BeerStyleRepository repository;
    @Autowired
    private BeerStyleService service;

    @After
    public void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    public void getAll() {
        //given
        var beerStyles = List.of(
                beerStyleOf("name1", 10, -5),
                beerStyleOf("name2", 15, -8));
        repository.saveAll(beerStyles).collectList().block();
        //when
        var result = service.getAll().collectList().block();
        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(beerStyles.containsAll(result));
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundOnGetAllWhenNotExists() {
        service.getAll().blockFirst();
    }

    @Test
    public void create() {
        //given
        var beerStyle = beerStyleOf("name1", 10, -5);
        //when
        var result = service.create(beerStyle).block();
        var persitedBeerStyle = repository.findById(result.getId()).block();
        //then
        assertEquals(beerStyle, result);
        assertEquals(beerStyle, persitedBeerStyle);
        assertEquals(beerStyle.getMaxTemperature(), persitedBeerStyle.getMaxTemperature());
        assertEquals(beerStyle.getMinTemperature(), persitedBeerStyle.getMinTemperature());
    }

    @Test
    public void updateById() {
        //given
        var savedBeerStyle = repository.save(beerStyleOf("name1", 10, 5)).block();
        //when
        final BeerStyle beerStyleUpdate = beerStyleOf("new name", 15, 14);
        var result = service.updateById(savedBeerStyle.getId(), beerStyleUpdate).block();
        var persitedBeerStyle = repository.findById(result.getId()).block();
        //then
        assertEquals(savedBeerStyle.getId(), result.getId());
        assertEquals(persitedBeerStyle, result);
        assertEquals(persitedBeerStyle.getMaxTemperature(), result.getMaxTemperature());
        assertEquals(persitedBeerStyle.getMinTemperature(), result.getMinTemperature());
        assertEquals(result, beerStyleUpdate);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundWhenTryUpdateBeerStyleThatNotExists() {
        //given
        final BeerStyle beerStyleUpdate = beerStyleOf("new name", 15, 14);
        //when
        service.updateById("1", beerStyleUpdate).block();
    }

    @Test
    public void deleteById() {
        //given
        var savedBeerStyle = repository.save(beerStyleOf("name1", 10, 5)).block();
        //when
        service.deleteById(savedBeerStyle.getId()).block();
        var result = repository.findById(savedBeerStyle.getId()).block();
        //then
        assertNull(result);
    }

    @Test
    public void findByCloserAvgTemperature() {
        //given
        final BeerStyle beerStyle1 = beerStyleOf("name1", 12, 6);
        final BeerStyle beerStyle2 = beerStyleOf("name2", 20, 10);
        var beerStyles = List.of(beerStyle1,beerStyle2);
        repository.saveAll(beerStyles).collectList().block();

        //when
        var result = service.findByCloserAvgTemperature(12).block();
        //then
        assertEquals(beerStyle1, result);
        //when
        var secondResult = service.findByCloserAvgTemperature(13).block();
        //then
        assertEquals(beerStyle2, secondResult);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundWhenTryFindByTemperatureAndNotExistsAnyBeerStyle() {
        service.findByCloserAvgTemperature(12).block();
    }
}