package subway.route.domain.fare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FareConfiguration {

    @Bean
    public FarePolicy farePolicy() {
        return new FarePoliciesComposite(List.of(
                new DistanceFarePolicy(),
                new LineAdditionalFarePolicy(),
                new AgeDiscountFarePolicy()
        ));
    }
}
