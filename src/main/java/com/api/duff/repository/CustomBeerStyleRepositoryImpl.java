package com.api.duff.repository;

import com.api.duff.domain.BeerStyle;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Abs.absoluteValueOf;
import static org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Subtract;

public class CustomBeerStyleRepositoryImpl implements CustomBeerStyleRepository {

    private ReactiveMongoTemplate mongoTemplate;

    public CustomBeerStyleRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public Mono<BeerStyle> findByTemperature(int temperature) {
        Aggregation aggregation = newAggregation(
                project( "name", "maxGreatTemperature", "minGreatTemperature","avgTemperature")
                        .and(absoluteValueOf(Subtract.valueOf(temperature).subtract("avgTemperature"))).as("diff"),
                sort(by("diff")),
                limit(1L)
        );
        return mongoTemplate.aggregate(aggregation, "beer-styles", BeerStyle.class).elementAt(0);
    }
}
