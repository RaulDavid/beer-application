package com.api.duff.controller;

import com.api.duff.DuffApplicationTests;
import com.api.duff.dto.BeerStyleDto;
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
import reactor.core.publisher.Mono;

import java.util.List;

import static com.api.duff.domain.BeerStyle.beerStyleOf;
import static com.api.duff.dto.BeerStyleDto.beerStyleDtoOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class BeerStyleControllerTest extends DuffApplicationTests {


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BeerStyleRepository repository;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        webTestClient = webTestClient.mutate()
                .filter(documentationConfiguration(restDocumentation))
                .build();

        var beers = List.of(beerStyleOf("name 1", 10, 5),
                beerStyleOf("name 2", 10, 5));

        repository.saveAll(beers).collectList().block();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    @DisplayName("should get all beer styles")
    void getAll() {
        //when
        WebTestClient.ResponseSpec beerStylesSpec = webTestClient.get()
                .uri("/beer-styles")
                .exchange();

        //then
        beerStylesSpec.expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .consumeWith(document("beer-styles/get-all"));
    }

    @Test
    @DisplayName("should get beer style by id")
    void getById() {
        //given
        var beerStyle = repository.findAll().collectList().block().get(0);

        //when
        WebTestClient.ResponseSpec beerStylesSpec = webTestClient.get()
                .uri("/beer-styles/{id}", beerStyle.getId())
                .exchange();

        //then
        beerStylesSpec.expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(beerStyle.getId())
                .consumeWith(document("beer-styles/get-by-id"));
    }

    @Test
    @DisplayName("should create beer style")
    void create() {
        //given
        var beerStyleDto = beerStyleDtoOf(beerStyleOf("name 3", 10, 5));

        //when
        WebTestClient.ResponseSpec beerStylesSpec = webTestClient.post()
                .uri("/beer-styles")
                .body(Mono.just(beerStyleDto), BeerStyleDto.class)
                .exchange();

        //then
        beerStylesSpec.expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(beerStyleDto.getName())
                .consumeWith(document("beer-styles/create"));
    }

    @Test
    @DisplayName("should update beer style by id")
    void updateById() {
        //given
        var beerStyle = repository.findAll().collectList().block().get(0);
        var beerStyleDto = beerStyleDtoOf(beerStyleOf("new name", 10, 9));

        //when
        WebTestClient.ResponseSpec beerStylesSpec = webTestClient.put()
                .uri("/beer-styles/{id}", beerStyle.getId())
                .body(Mono.just(beerStyleDto), BeerStyleDto.class)
                .exchange();

        //then
        beerStylesSpec.expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(beerStyleDto.getName())
                .consumeWith(document("beer-styles/update"));
    }

    @Test
    @DisplayName("should delete beer style by id")
    void deleteById() {
        //given
        var beerStyle = repository.findAll().collectList().block().get(0);

        //when
        WebTestClient.ResponseSpec beerStylesSpec = webTestClient.delete()
                .uri("/beer-styles/{id}", beerStyle.getId())
                .exchange();

        //then
        beerStylesSpec.expectStatus()
                .isNoContent()
                .expectBody()
                .consumeWith(document("beer-styles/delete"));
        assertEquals(1, repository.findAll().collectList().block().size());
    }
}