package com.api.duff.service;

import com.api.duff.client.SpotifyClient;
import com.api.duff.domain.BeerStyle;
import com.api.duff.domain.PlaylistBeer;
import com.api.duff.exception.PlaylistBeerDifferentNameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.api.duff.domain.PlaylistBeer.playlistBeerOf;
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

    public Mono<PlaylistBeer> findByTemperature(int temperature, boolean checkNameParam) {
        log.info("finding playlist beer by temperature={}, checkNameParam={}", temperature, checkNameParam);
        return beerStyleService.findByCloserAvgTemperature(temperature)
                .flatMap(this::getPlaylistBeer)
                .doOnNext(playlistBeer -> checkPlaylistName(checkNameParam, playlistBeer));
    }

    private Mono<PlaylistBeer> getPlaylistBeer(BeerStyle beerStyle) {
        log.info("getting playlist beer by style={}", beerStyle);
        return spotifyClient.getPlaylistByName(beerStyle.getName())
                .switchIfEmpty(playlistBeerNotFound())
                .map(playlist -> playlistBeerOf(beerStyle, playlist))
                .doOnError(e -> log.error("error getting playlist beer, message={}", e.getMessage()));
    }

    private void checkPlaylistName(boolean checkNameParam, PlaylistBeer playlistBeer) {
        var playlistName = playlistBeer.getPlaylist().getName();
        var beerStyleName = playlistBeer.getBeerStyle().getName();
        if (checkNameParam && !containsIgnoreCase(playlistName, beerStyleName)) {
            throw new PlaylistBeerDifferentNameException();
        }
    }
}
