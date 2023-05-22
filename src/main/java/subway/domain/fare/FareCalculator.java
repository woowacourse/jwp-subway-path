package subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {
    private final DistancePolicy distancePolicy;

    public FareCalculator(final DistancePolicy distancePolicy) {
        this.distancePolicy = distancePolicy;
    }

    public int calculate(final int distance) {
        return distancePolicy.calculate(distance);
    }
}
