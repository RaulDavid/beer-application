package com.api.duff.service;

import com.api.duff.DuffApplicationTests;
import com.api.duff.exception.NotFoundException;
import com.api.duff.repository.BeerStyleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.api.duff.domain.BeerStyle.beerStyleOf;
import static com.api.duff.error.Errors.PLAYLIST_BEER_NOT_CONTAINS_BEER_STYLE_NAME;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaylistBeerServiceIT extends DuffApplicationTests {

    @Autowired
    private PlaylistBeerService service;
    @Autowired
    private BeerStyleRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    @DisplayName("should get playlist beer by closer avg temperature and playlist must contain beer style in name")
    void findByTemperature() {
        //given
        var beerStyle1 = beerStyleOf("rock", 12, 6);
        var beerStyle2 = beerStyleOf("pop", 20, 10);
        var beerStyles = List.of(beerStyle1, beerStyle2);
        repository.saveAll(beerStyles).collectList().block();

        //when
        var playlistBeer = service.findByTemperature(3, true).block();

        //then
        assertEquals(beerStyle1.getName(), playlistBeer.getBeerStyle().getName());
        var playListName = playlistBeer.getPlaylist().getName();
        assertTrue(containsIgnoreCase(playListName, beerStyle1.getName()));
    }

    @Test
    @DisplayName("should throw not found when not exist playlist that contains beer style name and checkName is true")
    void shouldNotFoundOnFindByTemperatureCheckNameTrue() {
        var beerStyle1 = beerStyleOf("rock pop 123", 12, 6);
        repository.save(beerStyle1).block();

        var notFoundException = assertThrows(
                NotFoundException.class,
                () -> service.findByTemperature(3, true).block());
        assertEquals(PLAYLIST_BEER_NOT_CONTAINS_BEER_STYLE_NAME, notFoundException.getMessage());
    }

    @Test
    @DisplayName("should not throw not found when not exist playlist that contains beer style name and checkName is false")
    void shouldNotFoundOnFindByTemperatureCheckNameFalse() {
        //given
        var beerStyle1 = beerStyleOf("rock pop 123", 12, 6);
        repository.save(beerStyle1).block();

        //when
        var result = service.findByTemperature(3, false).block();
        assertEquals(beerStyle1, result.getBeerStyle());
        assertNotNull(result);
    }
}