package subway.domain.subway.billing_policy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Path;
import subway.domain.section.Distance;

public final class BillingPolicyByDistance implements BillingPolicy {

    private static final int DEFAULT_FARE = 1250;
    private static final int EXTRA_FARE_UNIT = 100;

    enum DistanceLevel {

        LEVEL_ONE(10, 5),
        LEVEL_TWO(50, 8),
        ;

        private final int threshold;
        private final int distanceUnitForChargeFare;

        DistanceLevel(final int threshold, final int distanceUnitForChargeFare) {
            this.threshold = threshold;
            this.distanceUnitForChargeFare = distanceUnitForChargeFare;
        }

        private static List<DistanceLevel> getValuesSortedByAscendingThreshold() {
            return Arrays.stream(values())
                    .sorted((o1, o2) -> o2.threshold - o1.threshold)
                    .collect(Collectors.toUnmodifiableList());
        }

        private int calculateExtraFare(int distance) {
            final int extraDistance = distance - threshold;
            return EXTRA_FARE_UNIT * (extraDistance / distanceUnitForChargeFare);
        }
    }

    @Override
    public Fare calculateFare(final Path path) {
        Distance distance = path.getDistance();
        Fare fare = new Fare(DEFAULT_FARE);

        for (DistanceLevel distanceLevel : DistanceLevel.getValuesSortedByAscendingThreshold()) {
            if (distance.isGreaterThanOrEqualTo(distanceLevel.threshold)) {
                fare = fare.add(distanceLevel.calculateExtraFare(distance.getValue()));
                distance = new Distance(distanceLevel.threshold);
            }
        }
        return fare;
    }
}
