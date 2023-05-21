package subway.domain;

import subway.domain.section.Distance;

public class BasicFarePolicy implements FarePolicy {

    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int FIRST_DISTANCE = 10;
    private static final int SECOND_DISTANCE = 50;
    private static final double FIRST_DISTANCE_INCREMENT = 5.0;
    private static final double SECOND_DISTANCE_INCREMENT = 8.0;

    @Override
    public int calculate(final Distance distance) {
        if (distance.isNotLongerThan(FIRST_DISTANCE)) {
            return BASIC_FARE;
        }
        if (distance.isNotLongerThan(SECOND_DISTANCE)) {
            return BASIC_FARE + calculateOverFare(distance.getDistance(), FIRST_DISTANCE, FIRST_DISTANCE_INCREMENT);
        }
        return BASIC_FARE + calculateOverFare(SECOND_DISTANCE, FIRST_DISTANCE, FIRST_DISTANCE_INCREMENT) + calculateOverFare(distance.getDistance(), SECOND_DISTANCE, SECOND_DISTANCE_INCREMENT);
    }

    private int calculateOverFare(final int start, final int end, final double increment) {
        return (int) Math.ceil((start - end) / increment) * ADDITIONAL_FARE;
    }
}
