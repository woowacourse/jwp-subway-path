package subway.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.DistanceProportionFarePolicy;

@Configuration
public class FarePolicyConfiguration {
    @Bean
    public DistanceProportionFarePolicy distanceProportionFarePolicy() {
        return new DistanceProportionFarePolicy();
    }
}
