package subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class StandardFarePolicy implements FarePolicy {

    private static final int STANDARD_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int ADDITIONAL_FARE_BETWEEN_10_50 = 800;
    private static final int STANDARD_DISTANCE = 10;
    private static final int MAXIMUM_DISTANCE = 50;
    private static final int DISTANCE_UNIT_BETWEEN_10_50 = 5;
    private static final int DISTANCE_UNIT_AFTER_50 = 8;

    @Override
    public int calculate(final int distance) {
        int fare = STANDARD_FARE;

        if (distance <= STANDARD_DISTANCE) {
            return fare;
        }
        if (distance <= MAXIMUM_DISTANCE) {
            return (int) (Math.floor((distance - STANDARD_DISTANCE - 1) / DISTANCE_UNIT_BETWEEN_10_50) + 1)
                    * ADDITIONAL_FARE + fare;
        }
        return (int) (Math.floor((distance - MAXIMUM_DISTANCE - 1) / DISTANCE_UNIT_AFTER_50) + 1) * ADDITIONAL_FARE
                + ADDITIONAL_FARE_BETWEEN_10_50 + fare;
    }
}
