package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.DistanceFareStrategy;
import subway.domain.fare.FareStrategy;

@Configuration
public class FareConfiguration {

    @Bean
    public FareStrategy farePolicy() {
        return new DistanceFareStrategy();
    }
}
