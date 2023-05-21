package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.application.path.DifferentialDistancePricePolicy;
import subway.application.path.DistancePricePolicy;

@Configuration
public class PriceConfiguration {

    @Bean
    public DistancePricePolicy basicPricePolicy() {
        return new DifferentialDistancePricePolicy();
    }
}
