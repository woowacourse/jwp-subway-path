package subway.domain.fare;

import java.util.Objects;

public class Fare {
    private final int value;

    public Fare(final int value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("요금은 0 이상의 값이어야 합니다.");
        }
    }

    public Fare sum(Fare other) {
        return new Fare(this.value + other.value);
    }

    public int getValue() {
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
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
