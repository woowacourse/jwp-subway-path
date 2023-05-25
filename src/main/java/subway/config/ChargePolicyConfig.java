package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.policy.application.ChargePolicyComposite;
import subway.policy.infrastructure.AgeDiscountPolicy;
import subway.policy.infrastructure.DistanceFarePolicy;
import subway.policy.infrastructure.LineFarePolicy;

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
