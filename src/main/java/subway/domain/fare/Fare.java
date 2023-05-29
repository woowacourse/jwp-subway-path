package subway.domain.fare;

import java.util.Objects;

public class Fare {

    private final int fare;

    public Fare(final int fare) {
        validate(fare);
        this.fare = fare;
    }

    private void validate(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("요금은 0보다 작을 수 없습니다.");
        }
    }

    public Fare add(final Fare other) {
        return new Fare(this.fare + other.fare);
    }

    public Fare subtract(final Fare other) {
        return new Fare(this.fare - other.fare);
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
