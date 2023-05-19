package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.application.path.BasicPricePolicy;
import subway.application.path.PricePolicy;

@Configuration
public class PriceConfiguration {

    @Bean
    public PricePolicy basicPricePolicy() {
        return new BasicPricePolicy();
    }
}
