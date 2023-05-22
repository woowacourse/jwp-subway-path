package subway.domain;

import java.util.Objects;
import subway.exception.IllegalDistanceException;

public class Distance {

    private static final double MIN_VALUE = 0;
    public static final Distance MIN_DISTANCE = new Distance(MIN_VALUE);
    private final double value;

    private Distance(double value) {
        this.value = value;
    }

    public static Distance from(double value) {
        validate(value);
        return new Distance(value);
    }

    private static void validate(double value) {
        if (value <= MIN_VALUE) {
            throw new IllegalDistanceException("구간의 길이는 0보다 커야합니다.");
        }
    }

    public Distance add(Distance other) {
        return new Distance(this.value + other.value());
    }

    public Distance subtract(Distance other) {
        return new Distance(this.value - other.value);
    }

    public Distance divideAndCeil(Distance other) {
        double dividedValue = this.value / other.value;
        return new Distance(Math.ceil(dividedValue));
    }

    public boolean isLessThanOrEqualTo(Distance other) {
        return this.value <= other.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Distance that = (Distance) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public double value() {
        return value;
    }
}
