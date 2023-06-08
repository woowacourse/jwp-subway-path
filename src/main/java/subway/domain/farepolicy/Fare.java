package subway.domain.farepolicy;

import java.util.Objects;

public class Fare {

    private static final int ZERO = 0;

    final int value;

    public Fare(final int value) {
        this.value = value;
    }

    private void validateFare(final int value) {
        if (value < ZERO) {
            throw new IllegalArgumentException("요금이 음수가 될 수 없습니다.");
        }
    }

    public Fare add(final Fare fare) {
        return new Fare(this.value + fare.value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
