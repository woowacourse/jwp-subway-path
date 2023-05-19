package subway.domain;

import java.util.Objects;
import subway.exception.section.NegativeDistanceValueException;

public class Distance {

    private final int value;

    public Distance(int value) {
        validateMinValue(value);
        this.value = value;
    }

    private void validateMinValue(int value) {
        if (value < 0) {
            throw new NegativeDistanceValueException(value);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
