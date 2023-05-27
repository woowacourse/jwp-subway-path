package subway.domain.fare;

import org.springframework.stereotype.Component;
import subway.domain.route.Path;

@Component
public class DistanceBasedExtraFarePolicy implements ExtraFarePolicy {
    private static final int DEFAULT_EXTRA_FARE_UNIT = 100;
    private static final int BASE_DISTANCE = 10;
    private static final int MIDDLE_EXTRA_DISTANCE = 40;
    private static final int MIDDLE_EXTRA_DISTANCE_UNIT = 5;
    private static final int LONG_EXTRA_DISTANCE_UNIT = 8;

    @Override
    public Fare calculateExtraFare(final Path path) {
        final Fare fare = new Fare(0);

        int extraDistance = path.getDistance() - BASE_DISTANCE;
        if (extraDistance <= 0) {
            return fare;
        }

        if (extraDistance >= MIDDLE_EXTRA_DISTANCE) {
            return fare.add(calculateExtraUnitFare(MIDDLE_EXTRA_DISTANCE, MIDDLE_EXTRA_DISTANCE_UNIT))
                    .add(calculateExtraUnitFare(extraDistance - MIDDLE_EXTRA_DISTANCE, LONG_EXTRA_DISTANCE_UNIT));
        }

        return fare.add(calculateExtraUnitFare(extraDistance, MIDDLE_EXTRA_DISTANCE_UNIT));
    }

    private Fare calculateExtraUnitFare(final int extraDistance, final int distanceUnit) {
        int extraUnitFare = (int) Math.ceil(extraDistance / (double) distanceUnit) * DEFAULT_EXTRA_FARE_UNIT;
        return new Fare(extraUnitFare);
    }
}
