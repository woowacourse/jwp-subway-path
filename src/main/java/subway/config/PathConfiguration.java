package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.BaseFarePolicy;
import subway.domain.fare.DistanceBasedExtraFarePolicy;
import subway.domain.fare.FarePolicies;

@Configuration
public class PathConfiguration {
    @Bean
    public FarePolicies farePolicies() {
        return new FarePolicies(
                new BaseFarePolicy(),
                List.of(new DistanceBasedExtraFarePolicy())
        );
    }
}
