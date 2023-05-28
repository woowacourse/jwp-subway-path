package subway.domain.fare;

import java.util.Objects;
import subway.exception.ErrorMessage;
import subway.exception.InvalidException;

public class Fare {
    private final int value;

    public Fare(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < 0) {
            throw new InvalidException(ErrorMessage.INVALID_NEGATIVE_FARE);
        }
    }

    public Fare add(final Fare fare) {
        return new Fare(value + fare.value);
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
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
