package com.api.duff.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static com.api.duff.error.Errors.BEER_STYLE_CONFLICT;
import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(value = CONFLICT, reason = BEER_STYLE_CONFLICT)
public class BeerStyleConflictException extends RuntimeException {

    public BeerStyleConflictException() {
        super(BEER_STYLE_CONFLICT);
    }
}
