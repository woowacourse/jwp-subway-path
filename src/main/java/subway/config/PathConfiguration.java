package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.fare.FareCalculator;

@Configuration
public class PathConfiguration {

    @Bean
    public FareCalculator fareCalculator() {
        return new FareCalculator(new DistanceFarePolicy());
    }
}
