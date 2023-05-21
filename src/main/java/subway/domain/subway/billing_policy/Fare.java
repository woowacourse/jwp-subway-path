package subway.domain.subway.billing_policy;

import java.util.Objects;
import subway.exception.InvalidFareException;

public final class Fare {

    private final int value;

    public Fare(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < 0) {
            throw new InvalidFareException("요금은 0이상의 값이어야합니다.");
        }
    }

    public Fare add(final Fare target) {
        return add(target.getValue());
    }

    public Fare add(final int targetValue) {
        return new Fare(value + targetValue);
    }

    public Fare subtract(final Fare target) {
        return subtract(target.getValue());
    }

    public Fare subtract(final int targetValue) {
        return new Fare(value - targetValue);
    }

    public Fare multiply(final Fare target) {
        return multiply(target.getValue());
    }

    public Fare multiply(final double targetValue) {
        return new Fare((int) Math.round(value * targetValue));
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
