package subway.domain.vo;

import subway.exception.BusinessException;

public class Distance {

    private final long value;

    public Distance(final long value) {
        validate(value);
        this.value = value;
    }

    public static Distance from(final long value) {
        return new Distance(value);
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

    public boolean isExceedThan(final Distance distance) {
        return value > distance.value;
    }

    public boolean isLessThan(final Distance distance) {
        return value <= distance.value;
    }

    public boolean isMoreThan(final Distance distance) {
        return value >= distance.value;
    }

    public long getValue() {
        return value;
    }
}
