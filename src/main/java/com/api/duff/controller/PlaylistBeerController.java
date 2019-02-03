package com.api.duff.controller;

import com.api.duff.dto.PlaylistBeerDto;
import com.api.duff.service.PlaylistBeerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
public class PlaylistBeerController {

    private final PlaylistBeerService service;

    public PlaylistBeerController(PlaylistBeerService service) {
        this.service = service;
    }

    @ResponseStatus(OK)
    @GetMapping(value = "/playlist-beers/beer-styles", params = "temperature")
    public Mono<PlaylistBeerDto> getByTemperature(@RequestParam int temperature,
                                                  @RequestParam(defaultValue = "true") boolean checkName) {
        return service.findByTemperature(temperature, checkName)
                .map(PlaylistBeerDto::playlistBeerDtoOf)
                .doOnError(e -> log.error("error mapping playlist beer dto, message={}", e.getMessage()));
    }
}
