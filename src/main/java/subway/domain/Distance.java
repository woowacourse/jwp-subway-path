package subway.domain;

import subway.domain.exception.NotPositiveDistanceException;

public class Distance {

    private final int value;

    public Distance(int value) {
        validatePositive(value);
        this.value = value;
    }

    private void validatePositive(int value) {
        if (value <= 0) {
            throw new NotPositiveDistanceException();
        }
    }

    public Distance gapBetween(Distance other) {
        int difference = Math.abs(value - other.value);
        return new Distance(difference);
    }

    public int getValue() {
        return value;
    }

    public boolean isLongerThan(Distance other) {
        return value > other.value;
    }

    public int calculateFare() {
        return FarePolicy.of(value).calculate(value);
    }

    public Distance sum(Distance other) {
        return new Distance(value + other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Distance distance = (Distance)o;

        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
