package subway.domain.fare.strategy;

import subway.domain.section.Distance;

public enum FareDistanceType {

    FIVE(new Distance(5)),
    EIGHT(new Distance(8)),
    TEN(new Distance(10)),
    FIFTY(new Distance(50));

    private final Distance distance;

    FareDistanceType(final Distance distance) {
        this.distance = distance;
    }

    public Distance distance() {
        return distance;
    }
}
