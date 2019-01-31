package com.api.duff.repository;

import com.api.duff.domain.BeerStyle;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Abs;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.aggregation.AccumulatorOperators.Avg.avgOf;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Abs.absoluteValueOf;
import static org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Subtract;

public class CustomBeerStyleRepositoryImpl implements CustomBeerStyleRepository {

    private static final String NAME_FIELD = "name";
    private static final String MAX_TEMPERATURE_FIELD = "maxTemperature";
    private static final String MIN_TEMPERATURE_FIELD = "minTemperature";
    private static final String DIFFERENCE_BETWEEN_AVG_FIELD = "differenceBetweenAvg";
    private static final long FIRST_ELEMENT = 1L;
    private static final String BEER_STYLES_COLLECTION = "beer-styles";

    private ReactiveMongoTemplate mongoTemplate;

    public CustomBeerStyleRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public Mono<BeerStyle> findByTemperature(int temperature) {
        Aggregation aggregation = newAggregation(
                project(getBeerStyleFields())
                        .and(getDifferenceOfAvg(temperature))
                        .as(DIFFERENCE_BETWEEN_AVG_FIELD),
                sort(by(DIFFERENCE_BETWEEN_AVG_FIELD, NAME_FIELD)),
                limit(FIRST_ELEMENT)
        );
        return mongoTemplate.aggregate(aggregation, BEER_STYLES_COLLECTION, BeerStyle.class).next();
    }

    private String[] getBeerStyleFields() {
        return new String[]{NAME_FIELD, MAX_TEMPERATURE_FIELD, MIN_TEMPERATURE_FIELD};
    }

    private Abs getDifferenceOfAvg(int temperature) {
        return absoluteValueOf(Subtract.valueOf(temperature)
                .subtract(avgOf(MAX_TEMPERATURE_FIELD).and(MIN_TEMPERATURE_FIELD))
        );
    }
}
