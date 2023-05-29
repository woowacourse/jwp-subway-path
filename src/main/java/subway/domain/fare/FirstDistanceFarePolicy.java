package subway.domain.fare;

import subway.domain.section.Distance;

public class FirstDistanceFarePolicy extends DistanceFarePolicy{

    private static final int DISTANCE_CRITERIA = 10;
    private static final int FARE = 1250;
    private static final DistanceFarePolicy NEXT_FARE_POLICY = new SecondDistanceFarePolicy();

    @Override
    public int calculate(final Distance distance) {
        if (distance.isNotLongerThan(DISTANCE_CRITERIA)) {
            return FARE;
        }
        return FARE + NEXT_FARE_POLICY.calculate(distance.subtract(DISTANCE_CRITERIA));
    }
}
