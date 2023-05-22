package subway.domain.vo;

import java.util.Objects;
import subway.exception.vo.DistanceException;

public class Distance {
    private final double value;

    public Distance(double value) {
        validate(value);
        this.value = value;
    }

    private void validate(double distance) {
        if (distance < 0) {
            throw new DistanceException();
        }
    }

    public Distance add(Distance other) {
        return new Distance(this.value + other.value);
    }

    public Distance substract(Distance other) {
        return new Distance(this.value - other.value);
    }

    public Distance divide(Distance other) {
        return new Distance(this.value / other.value);
    }
    public boolean isSmallerThan(Distance other) {
        return this.getValue() < other.getValue();
    }

    public boolean isSmallOrEqualThan(Distance other) {
        return this.getValue() <= other.getValue();
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
        return Double.compare(distance.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public double getValue() {
        return value;
    }
}
