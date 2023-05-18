package subway.domain.fare;

import java.util.Objects;

public class Fare {

    private final double fare;

    public Fare(final double fare) {
        this.fare = fare;
    }

    public Fare add(final Fare fare) {
        return new Fare(this.fare + fare.fare);
    }

    public Fare subtract(final Fare fare) {
        return new Fare(this.fare - fare.fare);
    }

    public Fare multiply(final Fare fare) {
        return new Fare(this.fare * fare.fare);
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

    public int fare() {
        return (int) fare;
    }
}
