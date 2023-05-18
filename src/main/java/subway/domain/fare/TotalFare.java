package subway.domain.fare;

import subway.domain.route.Distance;

public class TotalFare {

    private static final Fare BASIC_FARE = new Fare(1250);
    private static final Fare EXTRA_FARE = new Fare(100);
    private static final Distance EXTRA_FARE_DISTANCE_TEN = new Distance(10);
    private static final Distance EXTRA_FARE_DISTANCE_FIFTY = new Distance(50);
    private static final Distance FARE_DISTANCE_UNIT_FIVE = new Distance(5);
    private static final Distance FARE_DISTANCE_UNIT_EIGHT = new Distance(8);

    private final Fare totalFare;

    public TotalFare(final Distance distance) {
        this.totalFare = calculateFare(distance);
    }

    public TotalFare(final Distance distance, final Fare extraFare) {
        this.totalFare = calculateFare(distance).add(extraFare);
    }

    private Fare calculateFare(final Distance distance) {
        if (distance.lessThan(EXTRA_FARE_DISTANCE_TEN)) {
            return BASIC_FARE;
        }
        if (distance.lessAndEqualsThan(EXTRA_FARE_DISTANCE_FIFTY)) {
            final Distance extraDistance = distance.subtract(EXTRA_FARE_DISTANCE_TEN);
            return BASIC_FARE.add(calculateByDistance(extraDistance, FARE_DISTANCE_UNIT_FIVE));
        }
        final Distance fareSectionDistance = EXTRA_FARE_DISTANCE_FIFTY.subtract(EXTRA_FARE_DISTANCE_TEN);
        final Distance extraDistance = distance.subtract(EXTRA_FARE_DISTANCE_FIFTY);
        final Fare extraFare = calculateByDistance(fareSectionDistance, FARE_DISTANCE_UNIT_FIVE)
            .add(calculateByDistance(extraDistance, FARE_DISTANCE_UNIT_EIGHT));
        return BASIC_FARE.add(extraFare);
    }

    private Fare calculateByDistance(final Distance distance, final Distance unitDistance) {
        final Distance dividedDistance = distance.divideAndCeil(unitDistance);
        return new Fare(dividedDistance.distance()).multiply(EXTRA_FARE);
    }

    public Fare totalFare() {
        return totalFare;
    }
}
