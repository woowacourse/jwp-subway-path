package subway.domain.fare.strategy;

import subway.domain.fare.Fare;

public enum FareType {
    BASIC_FARE(new Fare(1250)),
    EXTRA_FARE(new Fare(100));

    private final Fare fare;

    FareType(final Fare fare) {
        this.fare = fare;
    }

    public Fare fare() {
        return fare;
    }
}
