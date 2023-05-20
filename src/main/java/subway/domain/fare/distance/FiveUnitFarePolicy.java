package subway.domain.fare.distance;

import org.springframework.stereotype.Component;
import subway.domain.fare.DistancePolicy;

@Component
public class FiveUnitFarePolicy implements DistancePolicy {
    private static final int UNIT = 5;
    private static final int FARE = 100;
    private static final int MIN_CALCULATE_RANGE = 10;
    private static final int MAX_CALCULATE_RANGE = 50;

    @Override
    public int calculate(final int distance) {
        if (distance >= MAX_CALCULATE_RANGE) {
            return (int) ((Math.ceil((MAX_CALCULATE_RANGE - MIN_CALCULATE_RANGE - 1) / UNIT) + 1) * FARE);
        }
        if (distance <= MIN_CALCULATE_RANGE) {
            return 0;
        }
        return (int) ((Math.ceil((distance - MIN_CALCULATE_RANGE - 1) / UNIT) + 1) * FARE);
    }
}
