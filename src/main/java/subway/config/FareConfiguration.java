package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.AgeFareStrategy;
import subway.domain.fare.DistanceFareStrategy;
import subway.domain.fare.FareStrategy;
import subway.domain.fare.FareStrategyComposite;
import subway.domain.fare.RouteFareStrategy;

@Configuration
public class FareConfiguration {

    @Bean
    public FareStrategy fareStrategy() {
        return new FareStrategyComposite(
                List.of(
                        new DistanceFareStrategy(),
                        new RouteFareStrategy(),
                        new AgeFareStrategy()
                )
        );
    }
}
