package subway.domain.fare;

import java.util.Objects;
import subway.exception.InvalidFareValueException;

public class Fare {
    private static final int MIN_VALUE = 1_250;

    private final int value;

    public Fare(final int value) {
        validateLowerThanMinimumValue(value);
        this.value = value;
    }

    private void validateLowerThanMinimumValue(final int value) {
        if (value < MIN_VALUE) {
            throw new InvalidFareValueException("최소 운임보다 적은 금액입니다.");
        }
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
