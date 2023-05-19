package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.fare.FarePolicy;

@Configuration
public class BeanConfig {

    @Bean
    public FarePolicy farePolicy() {
        return new DistanceFarePolicy();
    }
}
