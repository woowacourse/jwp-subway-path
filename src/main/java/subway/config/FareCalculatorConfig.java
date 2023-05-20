package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.business.domain.fare.DistanceFareStrategy;
import subway.business.domain.fare.FareCalculator;

import java.util.List;

@Configuration
public class FareCalculatorConfig {
    @Bean
    FareCalculator fareCalculator() {
        DistanceFareStrategy Over10KmDistanceFareStrategy = new DistanceFareStrategy(5, 50, 10);
        DistanceFareStrategy Over50KmDistanceFareStrategy = new DistanceFareStrategy(8, 50);
        return new FareCalculator(List.of(Over10KmDistanceFareStrategy, Over50KmDistanceFareStrategy));
    }
}
