package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.BasicFarePolicy;
import subway.domain.fare.FarePolicy;

@Configuration
public class FareConfiguration {

    @Bean
    public FarePolicy FarePolicy() {
        return new BasicFarePolicy();
    }
}
