package subway.application.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.DistanceProportionFarePolicy;
import subway.domain.fare.TotalDistanceFareCalculator;

@Configuration
public class FarePolicyConfiguration {
    @Bean
    public TotalDistanceFareCalculator distanceProportionFarePolicy() {
        final DistanceProportionFarePolicy firstExtraRangePolicy
                = new DistanceProportionFarePolicy(10, 50, 5);
        final DistanceProportionFarePolicy secondExtraRangePolicy
                = new DistanceProportionFarePolicy(50, 1_000_000, 8);

        return new TotalDistanceFareCalculator(List.of(
                firstExtraRangePolicy,
                secondExtraRangePolicy
        ));
    }
}
