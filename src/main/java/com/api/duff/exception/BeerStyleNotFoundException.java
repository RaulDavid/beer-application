package com.api.duff.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static com.api.duff.error.Errors.BEER_STYLE_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND, reason = BEER_STYLE_NOT_FOUND)
public class BeerStyleNotFoundException extends RuntimeException {

    public BeerStyleNotFoundException() {
        super(BEER_STYLE_NOT_FOUND);
    }
}
