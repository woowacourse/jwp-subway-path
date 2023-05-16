package subway.application;

import org.springframework.stereotype.Component;
import subway.domain.fare.Fare;
import subway.domain.fare.TotalDistance;

@Component
public final class FareCalculator {

    private static final Fare BASIC_FARE = new Fare(1250);
    private static final Fare EXTRA_FARE = new Fare(100);
    private static final TotalDistance EXTRA_FARE_SECTION_TEN = new TotalDistance(10);
    private static final TotalDistance EXTRA_FARE_SECTION_FIFTY = new TotalDistance(50);
    private static final TotalDistance FARE_SECTION_UNIT_FIVE = new TotalDistance(5);
    private static final TotalDistance FARE_SECTION_UNIT_EIGHT = new TotalDistance(8);

    public Fare calculateFare(final TotalDistance distance) {
        if (distance.lessThan(EXTRA_FARE_SECTION_TEN)) {
            return BASIC_FARE;
        }
        if (distance.lessAndEqualsThan(EXTRA_FARE_SECTION_FIFTY)) {
            final TotalDistance extraDistance = distance.subtract(EXTRA_FARE_SECTION_TEN);
            return BASIC_FARE.add(calculateByDistance(extraDistance, FARE_SECTION_UNIT_FIVE));
        }
        final TotalDistance fareSectionDistance = EXTRA_FARE_SECTION_FIFTY.subtract(EXTRA_FARE_SECTION_TEN);
        final TotalDistance extraDistance = distance.subtract(EXTRA_FARE_SECTION_FIFTY);
        final Fare extraFare = calculateByDistance(fareSectionDistance, FARE_SECTION_UNIT_FIVE)
            .add(calculateByDistance(extraDistance, FARE_SECTION_UNIT_EIGHT));
        return BASIC_FARE.add(extraFare);
    }

    private Fare calculateByDistance(final TotalDistance distance, final TotalDistance unitDistance) {
        final TotalDistance dividedDistance = distance.divideAndCeil(unitDistance);
        return new Fare(dividedDistance.distance()).multiply(EXTRA_FARE);
    }
}
