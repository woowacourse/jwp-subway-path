package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.application.price.DifferentialDistancePricePolicy;
import subway.application.price.PricePolicy;

@Configuration
public class PricePolicyConfiguration {

    @Bean
    public PricePolicy differentialDistancePricePolicy() {
        return new DifferentialDistancePricePolicy();
    }
}
