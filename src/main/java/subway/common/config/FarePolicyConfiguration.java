package subway.common.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.BaseFarePolicy;
import subway.domain.fare.DistanceAdditionalFarePolicy;
import subway.domain.fare.FarePolicy;
import subway.domain.fare.SumFarePolicy;

@Configuration
public class FarePolicyConfiguration {

    @Bean
    public FarePolicy farePolicy() {
        return new SumFarePolicy(List.of(
                new BaseFarePolicy(),
                new DistanceAdditionalFarePolicy()
        ));
    }
}
