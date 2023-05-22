package subway.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import subway.domain.fare.AgeBasedFarePolicy;
import subway.domain.fare.DistanceBasedFarePolicy;
import subway.domain.fare.FareCalculator;
import subway.domain.fare.FarePolicy;
import subway.domain.fare.LineBasedFarePolicy;
import subway.domain.path.JGraphTPathFinder;
import subway.domain.path.PathFinder;

@Configuration
public class CustomConfiguration {

    @Bean
    public FareCalculator fareCalculator() {
        int baseFare = 1250;
        List<FarePolicy> farePoliciesInApplicationOrder = List.of(
            new DistanceBasedFarePolicy(),
            new LineBasedFarePolicy(),
            new AgeBasedFarePolicy()
        );

        return new FareCalculator(baseFare, farePoliciesInApplicationOrder);
    }

    @Bean
    public PathFinder pathFinder() {
        return new JGraphTPathFinder();
    }
}
