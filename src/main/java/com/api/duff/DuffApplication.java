package com.api.duff;

import com.api.duff.domain.BeerStyle;
import com.api.duff.repository.BeerStyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DuffApplication implements CommandLineRunner {

	@Autowired
	BeerStyleRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(DuffApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var beers = List.of(
				BeerStyle.of("Weissbier", 3, -1),
				BeerStyle.of("Pilsens",	4, -2),
				BeerStyle.of("Weizenbier",	6, -4),
				BeerStyle.of("Red ale",	-5, 5),
				BeerStyle.of("India pale ale",	7, -6),
				BeerStyle.of("IPA",	10, -7),
				BeerStyle.of("Dunkel", 2, 2),
				BeerStyle.of("Imperial Stouts",	13, -10),
				BeerStyle.of("Brown ale",	14, 0)
		);
		repository.saveAll(beers).collectList().block();

	}
}

