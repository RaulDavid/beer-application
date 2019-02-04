package com.api.duff.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static com.api.duff.error.Errors.PLAYLIST_BEER_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND, reason = PLAYLIST_BEER_NOT_FOUND)
public class PlaylistBeerNotFoundException extends RuntimeException {

    public PlaylistBeerNotFoundException() {
        super(PLAYLIST_BEER_NOT_FOUND);
    }
}
