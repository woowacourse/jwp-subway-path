package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.fare.FarePolicies;
import subway.domain.fare.FarePolicy;
import subway.domain.fare.LineFarePolicy;

@Configuration
public class FareConfiguration {

    @Bean
    public FarePolicy farePolicy() {
        return new FarePolicies(List.of(new LineFarePolicy(), new DistanceFarePolicy()));
    }
}
