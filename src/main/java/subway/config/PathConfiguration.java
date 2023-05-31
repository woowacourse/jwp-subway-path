package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.*;

import java.util.List;

@Configuration
public class PathConfiguration {

    @Bean
    public List<FarePolicy> farePolicies() {
        return List.of(new BasicFarePolicy(), new DistanceFarePolicy(), new LineSurchargeFarePolicy());
    }

    @Bean
    public FareCalculator fareCalculator() {
        return new FareCalculator(farePolicies());
    }
}
