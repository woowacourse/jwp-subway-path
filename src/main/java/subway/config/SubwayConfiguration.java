package subway.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import subway.domain.fare.strategy.discount.AgeDiscountStrategy;
import subway.domain.fare.strategy.charge.DistanceChargeStrategy;
import subway.domain.fare.FareCalculator;
import subway.domain.fare.strategy.charge.LineChargeStrategy;
import subway.domain.path.strategy.PathFindByDistanceStrategy;
import subway.domain.path.strategy.PathFindStrategy;

@Configuration
public class SubwayConfiguration {

    @Bean
    public FareCalculator fareCalculator() {
        return new FareCalculator(
            List.of(new DistanceChargeStrategy(), new LineChargeStrategy()),
            List.of(new AgeDiscountStrategy())
        );
    }

    @Bean
    public PathFindStrategy pathFindStrategy() {
        return new PathFindByDistanceStrategy();
    }
}
