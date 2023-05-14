package subway.domain.vo;

import subway.exception.BusinessException;

public class Distance {

    private final long value;

    public Distance(final long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final long value) {
        if (value <= 0L) {
            throw new BusinessException("거리는 1 이상이어야합니다.");
        }
    }

    public Distance plus(final Distance distance) {
        return new Distance(value + distance.getValue());
    }

    public Distance minus(final Distance distance) {
        return new Distance(value - distance.getValue());
    }

    public long getValue() {
        return value;
    }
}
