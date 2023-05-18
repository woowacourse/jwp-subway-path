package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.application.BasicPricePolicy;
import subway.application.PricePolicy;

@Configuration
public class PriceConfiguration {

    @Bean
    public PricePolicy basicPricePolicy() {
        return new BasicPricePolicy();
    }
}
