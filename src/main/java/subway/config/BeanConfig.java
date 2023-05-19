package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.DistanceFarePolicy;
import subway.domain.FarePolicy;

@Configuration
public class BeanConfig {

    @Bean
    public FarePolicy farePolicy() {
        return new DistanceFarePolicy();
    }
}
