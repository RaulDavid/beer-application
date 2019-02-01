package com.api.duff.service;

import com.api.duff.client.SpotifyClient;
import com.api.duff.domain.BeerStyle;
import com.api.duff.domain.PlaylistBeer;
import com.api.duff.repository.BeerStyleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.api.duff.domain.PlaylistBeer.playlistBeerOf;

@Slf4j
@Service
public class BeerStyleService {

    private final BeerStyleRepository repository;
    private final SpotifyClient spotifyClient;

    public BeerStyleService(BeerStyleRepository repository, SpotifyClient spotifyClient) {
        this.repository = repository;
        this.spotifyClient = spotifyClient;
    }

    public Flux<BeerStyle> getAll() {
        return repository.findAll();
    }

    public Mono<BeerStyle> create(BeerStyle beerStyle) {
        log.info("creating beer style, beerStyle={}", beerStyle);
        return repository.save(beerStyle)
                .doOnError(e -> log.error("error saving beerStyle, beerStyle={}, message={}", beerStyle, e.getMessage()));
    }

    public Mono<BeerStyle> updateById(String id, BeerStyle beerStyle) {
        beerStyle.setId(id);
        log.info("updating beer style by id={} beerStyle={}", id, beerStyle);
        return repository.save(beerStyle)
                .doOnError(e -> log.error("error updating beerStyle, id={}, beerStyle={}, message={}", id, beerStyle, e.getMessage()));
    }

    public Mono<Void> deleteById(String id) {
        log.info("deleting beer style by id={}", id);
        return repository.deleteById(id)
                .doOnError(e -> log.error("error deleting by id, message={}", e.getMessage()));
    }

    public Mono<PlaylistBeer> findByTemperature(int temperature) {
        log.info("finding playlist beer by temperature={}", temperature);
        return repository.findByTemperature(temperature)
                .doOnError(e -> log.error("error finding by temperature, message={}", e.getMessage()))
                .flatMap(this::getPlaylistBeer);
    }

    private Mono<PlaylistBeer> getPlaylistBeer(BeerStyle beerStyle) {
        log.info("getting playlist beer by style={}", beerStyle);
        return spotifyClient.getPlaylistByName(beerStyle.getName())
                .map(playlist -> playlistBeerOf(beerStyle, playlist))
                .doOnError(e -> log.error("error getting playlist beer, message={}", e.getMessage()));
    }
}
