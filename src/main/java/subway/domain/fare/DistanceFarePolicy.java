package subway.domain.fare;

import static java.lang.Math.max;
import static java.lang.Math.min;

import subway.domain.path.Path;

public class DistanceFarePolicy implements FarePolicy {

    private static final int NO_EXTRA_FARE_DISTANCE = 10;
    private static final int EXTRA_FARE_SEGMENT = 50;
    private static final int EXTRA_FARE_BY_UNIT = 100;
    private static final int LESS_THAN_OR_EQUAL_EXTRA_FARE_UNIT = 5;
    private static final int MORE_THAN_EXTRA_FARE_UNIT = 8;

    @Override
    public int calculate(final Path path, final Passenger passenger, final int fare) {
        final int distance = path.calculateTotalDistance();

        final int extraDistance = min(
                EXTRA_FARE_SEGMENT - NO_EXTRA_FARE_DISTANCE,
                max(0, distance - NO_EXTRA_FARE_DISTANCE)
        );
        final int moreThanExtraDistance = max(0, distance - EXTRA_FARE_SEGMENT);

        return fare + calculateFare(extraDistance, LESS_THAN_OR_EQUAL_EXTRA_FARE_UNIT) +
                calculateFare(moreThanExtraDistance, MORE_THAN_EXTRA_FARE_UNIT);
    }

    private int calculateFare(final int distance, final int unit) {
        return calculateUnit(distance, unit) * EXTRA_FARE_BY_UNIT;
    }

    private int calculateUnit(final int distance, final int unit) {
        return (int) Math.ceil(distance / (double) unit);
    }
}
