package subway.domain.fare.distance;

import org.springframework.stereotype.Component;
import subway.domain.fare.DistancePolicy;

@Component
public class BasicFarePolicy implements DistancePolicy {
    private static final int BASIC_FARE = 1250;

    @Override
    public int calculate(final int distance) {
        return BASIC_FARE;
    }

}
