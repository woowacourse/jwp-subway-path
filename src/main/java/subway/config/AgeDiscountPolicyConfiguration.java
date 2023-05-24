package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.application.discount.AgeDiscountPolicy;
import subway.application.discount.KidsDiscountPolicy;
import subway.application.discount.TeenagersDiscountPolicy;

@Configuration
public class AgeDiscountPolicyConfiguration {

    @Bean
    public AgeDiscountPolicy kidsDiscountPolicy() {
        return new KidsDiscountPolicy();
    }

    @Bean
    public AgeDiscountPolicy teenagersDiscountPolicy() {
        return new TeenagersDiscountPolicy();
    }
}
