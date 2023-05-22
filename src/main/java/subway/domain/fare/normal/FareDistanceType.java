package subway.domain.fare.normal;

import subway.domain.route.Distance;

public enum FareDistanceType {
    EXTRA_FARE_DISTANCE_TEN(new Distance(10)),
    EXTRA_FARE_DISTANCE_FIFTY(new Distance(50)),
    FARE_DISTANCE_UNIT_EIGHT(new Distance(8)),
    FARE_DISTANCE_UNIT_FIVE(new Distance(5));

    private final Distance distance;

    FareDistanceType(final Distance distance) {
        this.distance = distance;
    }

    public Distance distance() {
        return distance;
    }
}
