package com.api.duff.service;

import com.api.duff.client.SpotifyClient;
import com.api.duff.domain.BeerStyle;
import com.api.duff.domain.PlaylistBeer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.api.duff.domain.PlaylistBeer.playlistBeerOf;

@Slf4j
@Service
public class PlaylistBeerService {

    private final BeerStyleService beerStyleService;
    private final SpotifyClient spotifyClient;

    public PlaylistBeerService(BeerStyleService beerStyleService, SpotifyClient spotifyClient) {
        this.beerStyleService = beerStyleService;
        this.spotifyClient = spotifyClient;
    }

    public Mono<PlaylistBeer> findByTemperature(int temperature) {
        log.info("finding playlist beer by temperature={}", temperature);
        return beerStyleService.findByTemperature(temperature)
                .flatMap(this::getPlaylistBeer);
    }

    private Mono<PlaylistBeer> getPlaylistBeer(BeerStyle beerStyle) {
        log.info("getting playlist beer by style={}", beerStyle);
        return spotifyClient.getPlaylistByName(beerStyle.getName())
                .map(playlist -> playlistBeerOf(beerStyle, playlist))
                .doOnError(e -> log.error("error getting playlist beer, message={}", e.getMessage()));
    }
}
