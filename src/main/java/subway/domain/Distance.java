package subway.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import subway.application.exception.ExceptionMessages;
import subway.application.exception.InvalidDistanceException;

import java.util.Objects;

public class Distance {
    public static final int MIN_DISTANCE_VALUE = 0;
    private final int value;

    protected Distance(int value) {
        this.value = value;
    }

    public static Distance of(int value) {
        if (value <= MIN_DISTANCE_VALUE) {
            throw new InvalidDistanceException(ExceptionMessages.INVALID_DISTANCE);
        }
        return new Distance(value);
    }

    public Distance subtract(Distance distance) {
        return Distance.of(this.value - distance.value);
    }

    public Distance add(Distance distance) {
        return Distance.of(this.value + distance.value);
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Distance{" +
                "value=" + value +
                '}';
    }
}
