package subway.domain;

import java.util.Objects;

import subway.exception.DomainException;
import subway.exception.ExceptionType;

public class Distance {
    private final Integer distance;

    public Distance(Integer distance) {
        this.distance = validate(distance);
    }

    private Integer validate(Integer distance) {
        if (distance < 1) {
            throw new DomainException(ExceptionType.INVALID_DISTANCE);
        }
        return distance;
    }

    public Integer value() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Distance distance1 = (Distance)o;
        return Objects.equals(distance, distance1.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
