package subway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import subway.domain.fare.DistanceStrategy;
import subway.domain.fare.FareCalculator;
import subway.domain.fare.FareStrategy;

@Configuration
public class SubwayConfig implements WebMvcConfigurer {

    @Bean
    public FareStrategy fareStrategy() {
        return new DistanceStrategy();
    }

    @Bean
    public FareCalculator fareCalculator() {
        return new FareCalculator(fareStrategy());
    }
}
