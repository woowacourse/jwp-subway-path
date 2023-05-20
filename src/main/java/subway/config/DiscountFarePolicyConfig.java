package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.discount.ChildFarePolicy;
import subway.domain.fare.discount.DiscountFarePolicyComposite;
import subway.domain.fare.discount.TeenagerFarePolicy;

@Configuration
public class DiscountFarePolicyConfig {

    @Bean
    public DiscountFarePolicyComposite discountFarePolicies(
        final ChildFarePolicy childFarePolicy,
        final TeenagerFarePolicy teenagerFarePolicy
    ) {
        return new DiscountFarePolicyComposite(
            List.of(childFarePolicy, teenagerFarePolicy)
        );
    }
}
