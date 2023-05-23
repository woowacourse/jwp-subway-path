package subway.domain.fare;

import java.util.Objects;

public class Fare {

    private final int value;

    public Fare(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("요금은 음수일 수 없습니다.");
        }
    }

    public Fare add(final Fare other) {
        return new Fare(value + other.value);
    }

    public Fare multiply(final int value) {
        return new Fare(this.value * value);
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
        final Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value +
                '}';
    }
}
