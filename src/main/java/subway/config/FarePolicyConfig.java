package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.normal.BasicFarePolicy;
import subway.domain.fare.normal.FarePolicyComposite;
import subway.domain.fare.normal.UnitEightFarePolicy;
import subway.domain.fare.normal.UnitFiveFarePolicy;

@Configuration
public class FarePolicyConfig {

    @Bean
    public FarePolicyComposite farePolicies(
        final BasicFarePolicy basicFarePolicy,
        final UnitFiveFarePolicy unitFiveFarePolicy,
        final UnitEightFarePolicy unitEightFarePolicy
    ) {
        return new FarePolicyComposite(
            List.of(basicFarePolicy, unitFiveFarePolicy, unitEightFarePolicy)
        );
    }
}
