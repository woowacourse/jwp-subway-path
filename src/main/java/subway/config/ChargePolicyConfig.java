package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.policy.ChargePolicyComposite;
import subway.domain.policy.discount.AgeDiscountPolicy;
import subway.domain.policy.fare.DistanceFarePolicy;
import subway.domain.policy.fare.LineFarePolicy;

@Configuration
public class ChargePolicyConfig {

  @Bean
  public ChargePolicyComposite chargePolicyComposite() {
    return new ChargePolicyComposite(
        List.of(new DistanceFarePolicy(), new LineFarePolicy()),
        List.of(new AgeDiscountPolicy())
    );
  }
}
