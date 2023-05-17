package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.AgeDiscountFarePolicy;
import subway.domain.fare.BaseFarePolicy;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.fare.FarePolicy;
import subway.domain.fare.SubwayFarePolicy;

@Configuration
public class FareConfiguration {

    @Bean
    public FarePolicy farePolicy() {
        return new SubwayFarePolicy(List.of(
                new BaseFarePolicy(),
                new DistanceFarePolicy(),
                new AgeDiscountFarePolicy()
        ));
    }
}
