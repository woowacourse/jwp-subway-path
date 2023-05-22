package subway.domain.fare.distance;

import org.springframework.stereotype.Component;
import subway.domain.fare.DistancePolicy;

@Component
public class EightUnitFarePolicy implements DistancePolicy {
    private static final int UNIT = 8;
    private static final int FARE = 100;
    private static final int MIN_CALCULATE_RANGE = 50;

    @Override
    public int calculate(final int distance) {
        if (distance <= MIN_CALCULATE_RANGE) {
            return 0;
        }
        return (int) ((Math.ceil((distance - MIN_CALCULATE_RANGE - 1) / UNIT) + 1) * FARE);
    }
}
