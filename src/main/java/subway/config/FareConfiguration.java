package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.AgeDiscountPolicy;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.fare.FareCalculator;
import subway.domain.fare.FarePolicies;
import subway.domain.fare.LineFarePolicy;

@Configuration
public class FareConfiguration {

    @Bean
    public FareCalculator fareCalculator() {
        return new FareCalculator(new FarePolicies(List.of(new LineFarePolicy(), new DistanceFarePolicy())),
                new AgeDiscountPolicy());
    }
}
