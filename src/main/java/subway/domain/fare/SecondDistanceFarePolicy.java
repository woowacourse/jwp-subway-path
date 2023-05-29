package subway.domain.fare;

import subway.domain.section.Distance;

public class SecondDistanceFarePolicy extends DistanceFarePolicy{

    private static final int DISTANCE_CRITERIA = 40;
    private static final double INCREMENT = 5.0;
    private static final DistanceFarePolicy NEXT_FARE_POLICY = new LastDistanceFarePolicy();

    @Override
    public int calculate(final Distance distance) {
        if (distance.isNotLongerThan(DISTANCE_CRITERIA)) {
            return calculateAdditionalFare(distance.getDistance(), INCREMENT);
        }
        return calculateAdditionalFare(DISTANCE_CRITERIA, INCREMENT) + NEXT_FARE_POLICY.calculate(distance.subtract(DISTANCE_CRITERIA));
    }
}
