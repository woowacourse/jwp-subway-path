package subway.domain.fare;

import subway.domain.section.Distance;

public class LastDistanceFarePolicy extends DistanceFarePolicy {

    private static final double INCREMENT = 8.0;

    @Override
    public int calculate(final Distance distance) {
        return calculateAdditionalFare(distance.getDistance(), INCREMENT);
    }
}
