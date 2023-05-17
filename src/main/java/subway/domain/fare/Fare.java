package subway.domain.fare;

import java.util.Objects;

public class Fare {

    private int value;

    public Fare(final int value) {
        this.value = value;
    }

    public void sum(final int value) {
        this.value = this.value + value;
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
}
