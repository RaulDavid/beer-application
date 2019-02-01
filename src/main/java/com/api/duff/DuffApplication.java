package com.api.duff;

import com.api.duff.repository.BeerStyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

import static com.api.duff.domain.BeerStyle.beerStyleOf;

@SpringBootApplication
public class DuffApplication implements CommandLineRunner {

	@Autowired
	BeerStyleRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(DuffApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var weissbier = beerStyleOf("Weissbier", 10, -2);
		var pilsen = beerStyleOf("Pilsens", 10, -4);
		var weizenbier = beerStyleOf("Weizenbier", 10, -6);
		var redAle = beerStyleOf("Red ale", 10, -8);
		var beers = List.of(weissbier, pilsen, weizenbier, redAle);

		repository.saveAll(beers).collectList().block();
	}
}

