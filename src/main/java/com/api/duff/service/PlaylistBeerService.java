package com.api.duff.service;

import com.api.duff.client.SpotifyClient;
import com.api.duff.domain.BeerStyle;
import com.api.duff.domain.PlaylistBeer;
import com.api.duff.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.api.duff.domain.PlaylistBeer.playlistBeerOf;
import static com.api.duff.error.Errors.PLAYLIST_BEER_NOT_CONTAINS_BEER_STYLE_NAME;
import static com.api.duff.error.Errors.playlistBeerNotFound;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

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
        return beerStyleService.findByCloserAvgTemperature(temperature)
                .flatMap(this::getPlaylistBeer);
    }

    private Mono<PlaylistBeer> getPlaylistBeer(BeerStyle beerStyle) {
        log.info("getting playlist beer by style={}", beerStyle);
        return spotifyClient.getPlaylistByName(beerStyle.getName())
                .switchIfEmpty(playlistBeerNotFound())
                .doOnNext(playlist -> checkPlaylistName(beerStyle, playlist.getName()))
                .map(playlist -> playlistBeerOf(beerStyle, playlist))
                .doOnError(e -> log.error("error getting playlist beer, message={}", e.getMessage()));
    }

    private void checkPlaylistName(BeerStyle beerStyle, String playlistName) {
        if (!containsIgnoreCase(playlistName, beerStyle.getName())) {
            throw new NotFoundException(PLAYLIST_BEER_NOT_CONTAINS_BEER_STYLE_NAME);
        }
    }
}
