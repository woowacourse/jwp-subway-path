package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.fare.FareCalculator;
import subway.domain.fare.FarePolicy;

@Configuration
public class PathConfiguration {

    @Bean
    public FarePolicy farePolicy() {
        return new DistanceFarePolicy();
    }

    @Bean
    public FareCalculator fareCalculator() {
        return new FareCalculator(farePolicy());
    }
}
