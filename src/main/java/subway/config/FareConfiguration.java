package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.FarePolicy;
import subway.domain.fare.FirstDistanceFarePolicy;

@Configuration
public class FareConfiguration {

    @Bean
    public FarePolicy FarePolicy() {
        return new FirstDistanceFarePolicy();
    }
}
