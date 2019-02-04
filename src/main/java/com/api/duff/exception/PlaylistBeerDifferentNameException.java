package com.api.duff.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static com.api.duff.error.Errors.PLAYLIST_BEER_NOT_CONTAINS_BEER_STYLE_NAME;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND, reason = PLAYLIST_BEER_NOT_CONTAINS_BEER_STYLE_NAME)
public class PlaylistBeerDifferentNameException extends RuntimeException {

    public PlaylistBeerDifferentNameException() {
        super(PLAYLIST_BEER_NOT_CONTAINS_BEER_STYLE_NAME);
    }
}
