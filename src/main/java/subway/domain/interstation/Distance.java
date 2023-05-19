package subway.domain.interstation;

import java.util.Objects;
import subway.domain.interstation.exception.DistanceException;

public class Distance {

    private final long value;

    public Distance(final long value) {
        validate(value);
        this.value = value;
    }


    private void validate(final long value) {
        if (value <= 0) {
            throw new DistanceException("거리는 양수이어야 합니다.");
        }
    }

    public Distance add(final Distance distance) {
        return new Distance(value + distance.value);
    }

    public Distance minus(final Distance distance) {
        return new Distance(value - distance.value);
    }

    public Distance minus(final long distance) {
        return new Distance(value - distance);
    }

    public long getValue() {
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
        final Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
