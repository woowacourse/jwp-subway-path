package subway.domain.subway.billing_policy;

import subway.exception.InvalidFareException;

public final class Fare {

    private final int value;

    public Fare(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value <= 0) {
            throw new InvalidFareException("요금은 0보다 커야합니다.");
        }
    }

    public Fare add(final int targetValue) {
        return new Fare(value + targetValue);
    }

    public int getValue() {
        return value;
    }
}
