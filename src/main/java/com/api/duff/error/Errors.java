package com.api.duff.error;

import com.api.duff.exception.BeerStyleConflictException;
import com.api.duff.exception.BeerStyleNotFoundException;
import com.api.duff.exception.PlaylistBeerNotFoundException;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;

public class Errors {

    public final static String BEER_STYLE_NOT_FOUND = "beer style not found";
    public final static String BEER_STYLE_CONFLICT = "already exists a beer style with same name";
    public final static String PLAYLIST_BEER_NOT_FOUND = "playlist beer not found";
    public final static String PLAYLIST_BEER_NOT_CONTAINS_BEER_STYLE_NAME = "not exist spotify playlists that contains beer style name";

    public static <T> Mono<T> beerStyleNotFound() {
        return error(new BeerStyleNotFoundException());
    }

    public static <T> Mono<T> playlistBeerNotFound() {
        return error(new PlaylistBeerNotFoundException());
    }

    public static <T> Mono<T> beerStyleAlreadyExists() {
        return error(new BeerStyleConflictException());
    }
}
