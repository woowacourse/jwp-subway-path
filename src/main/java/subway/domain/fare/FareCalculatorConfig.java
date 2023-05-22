package subway.domain.fare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.strategy.AgeFareStrategy;
import subway.domain.fare.strategy.DistanceFareStrategy;
import subway.domain.fare.strategy.FareStrategy;
import subway.domain.fare.strategy.LineAdditionalFareStrategy;

import java.util.List;

@Configuration
public class FareCalculatorConfig {
    @Bean
    public FareCalculator fareCalculator() {
        final List<FareStrategy> fareStrategies = List.of(
                new DistanceFareStrategy(),
                new LineAdditionalFareStrategy(),
                new AgeFareStrategy());

        return new FareCalculator(fareStrategies);
    }
}
