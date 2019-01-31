package com.api.duff.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@EqualsAndHashCode(of = "name")
@Document("beer-styles")
public class BeerStyle implements Serializable {

    @Id
    private String id;

    @Indexed
    private String name;
    private int maxTemperature;
    private int minTemperature;

    private BeerStyle(String name, int maxTemperature, int minTemperature) {
        this.name = name;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
    }

    public static BeerStyle beerStyleOf(String name, int maxTemperature, int minTemperature) {
        return new BeerStyle(name, maxTemperature, minTemperature);
    }
}
