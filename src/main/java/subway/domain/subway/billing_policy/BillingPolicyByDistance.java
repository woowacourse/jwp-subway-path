package subway.domain.subway.billing_policy;

import subway.domain.Path;
import subway.domain.section.Distance;

public final class BillingPolicyByDistance implements BillingPolicy {

    private static final int DEFAULT_FARE = 1250;
    private static final int EXTRA_FARE = 100;
    private static final int LEVEL_ONE_DISTANCE_THRESHOLD = 10;
    private static final int LEVEL_ONE_DISTANCE_UNIT = 5;
    private static final int LEVEL_TWO_DISTANCE_THRESHOLD = 50;
    private static final int LEVEL_TWO_DISTANCE_UNIT = 8;

    @Override
    public Fare calculateFare(final Path path) {
        Distance distance = path.getDistance();
        Fare fare = new Fare(DEFAULT_FARE);

        if (distance.isGreaterThanOrEqualTo(LEVEL_TWO_DISTANCE_THRESHOLD)) {
            final Distance levelTwoExtraDistance = distance.subtract(LEVEL_TWO_DISTANCE_THRESHOLD);
            fare = fare.add(EXTRA_FARE * (levelTwoExtraDistance.getValue() / LEVEL_TWO_DISTANCE_UNIT));
            distance = new Distance(LEVEL_TWO_DISTANCE_THRESHOLD);
        }

        if (distance.isGreaterThanOrEqualTo(LEVEL_ONE_DISTANCE_THRESHOLD)) {
            final Distance levelOneExtraDistance = distance.subtract(LEVEL_ONE_DISTANCE_THRESHOLD);
            fare = fare.add(EXTRA_FARE * (levelOneExtraDistance.getValue() / LEVEL_ONE_DISTANCE_UNIT));
        }

        return fare;
    }
}
