package subway.line.domain.interstation;

import java.util.Objects;
import subway.line.domain.interstation.exception.DistanceException;

public class Distance {

    private final long value;

    public Distance(long value) {
        validate(value);
        this.value = value;
    }

    private void validate(long value) {
        if (value <= 0) {
            throw new DistanceException("거리는 양수이어야 합니다.");
        }
    }

    public Distance add(Distance distance) {
        return new Distance(value + distance.value);
    }

    public long getValue() {
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
