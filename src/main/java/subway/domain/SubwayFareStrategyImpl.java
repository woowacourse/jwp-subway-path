package subway.domain;

import java.util.stream.IntStream;

public class SubwayFareStrategyImpl implements SubwayFareStrategy {

    private static final Distance BASE_DISTANCE = new Distance(10);
    private static final Distance EXTRA_DISTANCE = new Distance(50);
    private static final int UNIT_DISTANCE_UNDER_EXTRA_DISTANCE = 5;
    private static final int UNIT_DISTANCE_OVER_EXTRA_DISTANCE = 8;

    private static final int EXTRA_FARE = 100;
    private static final int BASE_FARE = 1250;

    @Override
    public Fare calculteFare(Distance distance) {
        if (BASE_DISTANCE.isLongerThan(distance)) {
            return new Fare(BASE_FARE);
        }

        if (EXTRA_DISTANCE.isLongerThan(distance) || EXTRA_DISTANCE.equals(distance)) {
            return new Fare(BASE_FARE + getUnderExtraFare(distance));
        }

        return new Fare(BASE_FARE + getUnderExtraFare(EXTRA_DISTANCE) + getOverExtraFare(distance));
    }

    private int getOverExtraFare(Distance distance) {
        int overExtraDistance = distance.subtract(EXTRA_DISTANCE).getValue();
        return IntStream.range(0, overExtraDistance / UNIT_DISTANCE_OVER_EXTRA_DISTANCE)
                .reduce(0, (a, b) -> EXTRA_FARE);

    }

    private static int getUnderExtraFare(Distance distance) {
        int extraDistance = distance.subtract(BASE_DISTANCE).getValue();
        return IntStream.range(0, extraDistance / UNIT_DISTANCE_UNDER_EXTRA_DISTANCE)
                .reduce(0, (a, b) -> a + EXTRA_FARE);
    }

}
