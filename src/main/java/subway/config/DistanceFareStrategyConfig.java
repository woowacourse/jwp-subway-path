package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.business.domain.fare.DistanceFareStrategy;
import subway.business.domain.fare.DistanceFareStrategyImpl;

@Configuration
public class DistanceFareStrategyConfig {
    @Bean
    DistanceFareStrategy distanceFareStrategy() {
        return new DistanceFareStrategyImpl();
    }
}
