package subway.domain.fare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FareConfiguration {

    private final List<DistancePolicy> distancePolicies;

    public FareConfiguration(final List<DistancePolicy> distancePolicies) {
        this.distancePolicies = distancePolicies;
    }

    @Bean
    public DistancePolicy distancePolicy() {
        return new DiscountChain(distancePolicies);
    }
}
