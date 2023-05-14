package subway.domain.interstation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import subway.domain.interstation.exception.DistanceException;

@Getter
@ToString
@EqualsAndHashCode(of = "value")
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
}
