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
    private int maxGreatTemperature;
    private int minGreatTemperature;
    private double avgTemperature;

    private BeerStyle(String name, int maxGreatTemperature, int minGreatTemperature) {
        this.name = name;
        this.maxGreatTemperature = maxGreatTemperature;
        this.minGreatTemperature = minGreatTemperature;
        avgTemperature = calculateAvgTemperature(maxGreatTemperature, minGreatTemperature);
    }

    private double calculateAvgTemperature(int maxGreatTemperature, int minGreatTemperature) {
        return (maxGreatTemperature + minGreatTemperature) / 2;
    }

    public static BeerStyle of(String name, int maxTemperature, int minTemperature) {
        return new BeerStyle(name, maxTemperature, minTemperature);
    }
}
