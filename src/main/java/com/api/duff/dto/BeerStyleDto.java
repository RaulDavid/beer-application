package com.api.duff.dto;

import com.api.duff.domain.BeerStyle;
import lombok.Data;

import java.io.Serializable;

import static com.api.duff.domain.BeerStyle.beerStyleOf;

@Data
public class BeerStyleDto implements Serializable {

    private final String id;
    private final String name;
    private final int maxTemperature;
    private final int minTemperature;

    private BeerStyleDto(String id, String name, int maxTemperature, int minTemperature) {
        this.id = id;
        this.name = name;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
    }

    public static BeerStyleDto beerStyleDtoOf(BeerStyle beerStyle) {
        return new BeerStyleDto(
                beerStyle.getId(),
                beerStyle.getName(),
                beerStyle.getMaxTemperature(),
                beerStyle.getMinTemperature());
    }

    public BeerStyle toBeerStyle() {
        return beerStyleOf(name, maxTemperature, minTemperature);
    }

}
