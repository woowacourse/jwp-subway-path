package subway.domain.fare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.distance.FirstChain;
import subway.domain.fare.distance.SecondChain;
import subway.domain.fare.distance.ThirdChain;

@Configuration
public class FareConfiguration {
    @Bean
    public FareCalculator fareCalculator() {
        ThirdChain thirdChain = new ThirdChain();
        SecondChain secondChain = new SecondChain(thirdChain);
        FirstChain firstChain = new FirstChain(secondChain);
        return new FareCalculator(firstChain);
    }
}
