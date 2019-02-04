package com.api.duff.controller;

import com.api.duff.DuffApplicationTests;
import com.api.duff.repository.BeerStyleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.api.duff.domain.BeerStyle.beerStyleOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class PlaylistBeerControllerTest extends DuffApplicationTests {


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BeerStyleRepository repository;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        webTestClient = webTestClient.mutate()
                .filter(documentationConfiguration(restDocumentation)).build();

    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    @DisplayName("should get playlist beer by temperature with name that contains beer style")
    void getByTemperature() {
        //given
        var beers = List.of(beerStyleOf("rock", 11, 10),
                beerStyleOf("pop", 10, 5));
        repository.saveAll(beers).collectList().block();

        //when
        WebTestClient.ResponseSpec beerStylesSpec = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/playlist-beers/beer-styles")
                        .queryParam("temperature", 10)
                        .build())
                .exchange();

        //then
        beerStylesSpec.expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.beerStyle").isEqualTo(beers.get(0).getName())
                .consumeWith(document("playlist-beer/get-by-temperature"));
    }

    @Test
    @DisplayName("should get playlist beer without check playlist name")
    void getByTemperatureWithoutCheckName() {
        //given
        var beers = List.of(beerStyleOf("Brown ale", 11, 10),
                beerStyleOf("Dunkel", 10, 5));
        repository.saveAll(beers).collectList().block();
        var firstBeerName = beers.get(0).getName();

        //when
        WebTestClient.ResponseSpec beerStylesSpec = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/playlist-beers/beer-styles")
                        .queryParam("temperature", 10)
                        .queryParam("checkName", false)
                        .build())
                .exchange();

        //then
        beerStylesSpec.expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.beerStyle").isEqualTo(firstBeerName)
                .jsonPath("$.playlist.name").value(beerStyleName -> assertNotEquals(firstBeerName, beerStyleName))
                .consumeWith(document("playlist-beer/get-by-temperature-with-check-name-false"));
    }
}