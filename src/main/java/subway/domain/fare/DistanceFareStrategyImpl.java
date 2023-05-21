package subway.domain.fare;

import org.springframework.stereotype.Component;

import static java.lang.Math.floor;

@Component
public final class DistanceFareStrategyImpl implements DistanceFareStrategy {

    private static final int BASE_DISTANCE = 10;
    private static final int BASE_FARE = 1250;
    private static final int EXCESSIVE_DISTANCE = 50;

    private static final int UNIT_OF_ADDITIONAL_FARE = 100;
    private static final int UNIT_OF_FIRST_EXCESS_DISTANCE = 5;
    private static final int UNIT_OF_SECOND_EXCESS_DISTANCE = 8;

    @Override
    public int calculate(final long distance) {
        int fare = BASE_FARE;

        if (distance > BASE_DISTANCE) {
            fare += calculateFirstAdditionalFare(distance);
        }
        if (distance > EXCESSIVE_DISTANCE) {
            fare += calculateSecondAdditionalFare(distance);
        }

        return fare;
    }

    private int calculateFirstAdditionalFare(long distance) {
        distance -= BASE_DISTANCE;

        final double quotient = floor((double) distance / UNIT_OF_FIRST_EXCESS_DISTANCE);
        return (int) Math.min(10, quotient) * UNIT_OF_ADDITIONAL_FARE;
    }

    private int calculateSecondAdditionalFare(long distance) {
        distance -= BASE_DISTANCE + EXCESSIVE_DISTANCE;

        final double quotient = floor((double) distance / UNIT_OF_SECOND_EXCESS_DISTANCE);
        return (int) quotient * UNIT_OF_ADDITIONAL_FARE;
    }
}
