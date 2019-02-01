package com.api.duff.error;

import com.api.duff.exception.NotFoundException;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;

public class Errors {

    public static String BEER_STYLE_NOT_FOUND = "beer style not found";
    public static String PLAYLIST_BEER_NOT_FOUND = "playlist beer not found";
    public static String PLAYLIST_BEER_NOT_CONTAINS_BEER_STYLE_NAME = "not exist spotify playlists that contains beer style name";

    public static <T> Mono<T> beerStyleNotFound() {
        return error(new NotFoundException(BEER_STYLE_NOT_FOUND));
    }

    public static <T> Mono<T> playlistBeerNotFound() {
        return error(new NotFoundException(PLAYLIST_BEER_NOT_FOUND));
    }
}
