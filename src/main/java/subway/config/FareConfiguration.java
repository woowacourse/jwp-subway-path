package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.FareStrategy;
import subway.domain.fare.strategy.DistanceBasicFareStrategy;
import subway.domain.fare.strategy.DistanceFiftyStrategy;
import subway.domain.fare.strategy.DistanceTenFareStrategy;

@Configuration
public class FareConfiguration {

    @Bean
    public List<FareStrategy> farePolicy() {
        return List.of(
                new DistanceBasicFareStrategy(),
                new DistanceTenFareStrategy(),
                new DistanceFiftyStrategy()
        );
    }
}
