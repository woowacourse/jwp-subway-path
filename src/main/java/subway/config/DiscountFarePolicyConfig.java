package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.discount.DiscountFarePolicy;
import subway.domain.fare.discount.DiscountFarePolicyComposite;

@Configuration
public class DiscountFarePolicyConfig {

    @Bean
    public DiscountFarePolicyComposite discountFarePolicies(
        final DiscountFarePolicy childFarePolicy,
        final DiscountFarePolicy teenagerFarePolicy
    ) {
        return new DiscountFarePolicyComposite(
            List.of(childFarePolicy, teenagerFarePolicy)
        );
    }
}
