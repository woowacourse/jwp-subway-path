package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.application.price.DifferentialDistancePricePolicy;
import subway.application.price.LineExtraFeePricePolicy;
import subway.application.price.PricePolicy;
import subway.dao.LineDao;

@Configuration
public class PricePolicyConfiguration {

    @Bean
    public PricePolicy differentialDistancePricePolicy() {
        return new DifferentialDistancePricePolicy();
    }

    @Bean
    public PricePolicy lineExtraFeePricePolicy(LineDao lineDao) {
        return new LineExtraFeePricePolicy(lineDao);
    }
}
