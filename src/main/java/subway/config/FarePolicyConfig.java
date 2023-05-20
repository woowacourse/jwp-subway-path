package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.normal.FarePolicy;
import subway.domain.fare.normal.FarePolicyComposite;

@Configuration
public class FarePolicyConfig {

    @Bean
    public FarePolicyComposite farePolicies(
        final FarePolicy basicFarePolicy,
        final FarePolicy unitFiveFarePolicy,
        final FarePolicy unitEightFarePolicy
    ) {
        return new FarePolicyComposite(
            List.of(basicFarePolicy, unitFiveFarePolicy, unitEightFarePolicy)
        );
    }
}
