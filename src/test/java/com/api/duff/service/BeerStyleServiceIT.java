package com.api.duff.service;

import com.api.duff.DuffApplicationTests;
import com.api.duff.domain.BeerStyle;
import com.api.duff.exception.NotFoundException;
import com.api.duff.repository.BeerStyleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.api.duff.domain.BeerStyle.beerStyleOf;
import static com.api.duff.error.Errors.BEER_STYLE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BeerStyleServiceIT extends DuffApplicationTests {

    @Autowired
    private BeerStyleRepository repository;
    @Autowired
    private BeerStyleService service;

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    @DisplayName("should get all beer styles")
    void getAll() {
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

    @Test
    @DisplayName("should throw not found exception when get all beer styles and don't have any")
    void shouldThrowNotFoundOnGetAll() {
        var notFoundException = assertThrows(
                NotFoundException.class,
                () -> service.getAll().blockFirst());
        assertEquals(BEER_STYLE_NOT_FOUND, notFoundException.getMessage());
    }

    @Test
    @DisplayName("should create beer style")
    void create() {
        //given
        var beerStyle = beerStyleOf("name1", 10, -5);

        //when
        var result = service.create(beerStyle).block();
        var savedBeerStyle = repository.findById(result.getId()).block();

        //then
        assertEquals(beerStyle, result);
        assertEquals(beerStyle, savedBeerStyle);
        assertEquals(beerStyle.getMaxTemperature(), savedBeerStyle.getMaxTemperature());
        assertEquals(beerStyle.getMinTemperature(), savedBeerStyle.getMinTemperature());
    }

    @Test
    @DisplayName("should update beer style by id")
    void updateById() {
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

    @Test
    @DisplayName("should throw not found exception when try update beer style that not exists")
    void shouldThrowNotFoundOnUpdate() {
        //given
        var beerStyleUpdate = beerStyleOf("new name", 15, 14);

        //when
        var notFoundException = assertThrows(
                NotFoundException.class,
                () -> service.updateById("1", beerStyleUpdate).block());

        //then
        assertEquals(BEER_STYLE_NOT_FOUND, notFoundException.getMessage());
    }

    @Test
    @DisplayName("should delete by id")
    void deleteById() {
        //given
        var savedBeerStyle = repository.save(beerStyleOf("name1", 10, 5)).block();

        //when
        service.deleteById(savedBeerStyle.getId()).block();
        var result = repository.findById(savedBeerStyle.getId()).block();

        //then
        assertNull(result);
    }

    @Test
    @DisplayName("should find beer style by temperature considering closer temperature avg")
    void findByCloserAvgTemperature() {
        //given
        var beerStyle1 = beerStyleOf("name1", 12, 6);
        var beerStyle2 = beerStyleOf("name2", 20, 10);
        var beerStyles = List.of(beerStyle1, beerStyle2);
        repository.saveAll(beerStyles).collectList().block();

        //when
        var result = service.findByCloserAvgTemperature(10).block();
        //then
        assertEquals(beerStyle1, result);

        //when
        var secondResult = service.findByCloserAvgTemperature(14).block();
        //then
        assertEquals(beerStyle2, secondResult);
    }

    @Test
    @DisplayName("should throw not found exception when try find beer styles by temperature and don't have any")
    void shouldThrowNotFoundOnFindByTemperature() {
        var notFoundException = assertThrows(
                NotFoundException.class,
                () -> service.findByCloserAvgTemperature(12).block());
        assertEquals(BEER_STYLE_NOT_FOUND, notFoundException.getMessage());
    }
}