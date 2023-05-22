package subway.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import subway.domain.fare.AgeBasedFarePolicy;
import subway.domain.fare.DistanceBasedFarePolicy;
import subway.domain.fare.FareCalculator;
import subway.domain.fare.FarePolicy;
import subway.domain.fare.LineBasedFarePolicy;

@Configuration
public class FareConfiguration {

    private final static int BASE_FARE = 1250;
    private final static List<FarePolicy> FARE_POLICIES_IN_APPLICATION_ORDER = List.of(
        new DistanceBasedFarePolicy(),
        new LineBasedFarePolicy(),
        new AgeBasedFarePolicy()
    );

    @Bean
    public FareCalculator fareCalculator() {
        return new FareCalculator(BASE_FARE, FARE_POLICIES_IN_APPLICATION_ORDER);
    }
}
