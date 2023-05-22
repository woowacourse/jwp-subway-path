package subway.domain;

import java.util.Objects;

public class Distance {

    private final int value;

    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value <= 0 || value >= 100) {
            throw new IllegalArgumentException("역 간의 거리는 0~100 사이만 가능합니다.");
        }
    }

    public int getValue() {
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
        return getValue() == distance.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
