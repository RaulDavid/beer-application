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
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlaylistBeerServiceIT extends DuffApplicationTests {

    @Autowired
    private PlaylistBeerService service;
    @Autowired
    private BeerStyleRepository repository;

    @After
    public void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    public void findByTemperature() {
        //given
        final BeerStyle beerStyle1 = beerStyleOf("rock", 12, 6);
        final BeerStyle beerStyle2 = beerStyleOf("pop", 20, 10);
        var beerStyles = List.of(beerStyle1, beerStyle2);
        repository.saveAll(beerStyles).collectList().block();
        //when
        var playlistBeer = service.findByTemperature(3).block();
        //then
        assertEquals(beerStyle1.getName(), playlistBeer.getBeerStyle().getName());
        final String playListName = playlistBeer.getPlaylist().getName();
        assertTrue(containsIgnoreCase(playListName, beerStyle1.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowPlaylistBeerNotFoundWhenNotExistPlaylistWithBeerStyleName() {
        //given
        String name = "rock pop 123";
        final BeerStyle beerStyle1 = beerStyleOf(name, 12, 6);
        repository.save(beerStyle1).block();
        //when
        service.findByTemperature(3).block();
    }
}