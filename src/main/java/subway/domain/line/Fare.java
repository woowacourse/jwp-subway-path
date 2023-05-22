package subway.domain.line;

import subway.exception.InvalidFareException;

public class Fare {

    private static final int MINIMUM_VALUE = 0;

    private final int value;

    public Fare(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < MINIMUM_VALUE) {
            throw new InvalidFareException("노선 추가 요금은 " + MINIMUM_VALUE + "원보다 커야 합니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
