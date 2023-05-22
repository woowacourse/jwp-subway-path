package subway.line.domain.section.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import subway.common.exception.ExceptionMessages;
import subway.line.domain.section.domain.exception.InvalidDistanceException;

import java.util.Objects;

public class Distance {
    public static final double MIN_DISTANCE_VALUE = 0;
    private final double value;

    protected Distance(double value) {
        this.value = value;
    }

    public static Distance of(double value) {
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
    public double getValue() {
        return value;
    }

    public boolean isMoreThanOrEquals(Distance distance) {
        return distance.value <= this.value;
    }

    public boolean isLessThanOrEquals(Distance distance) {
        return this.value <= distance.value;
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
