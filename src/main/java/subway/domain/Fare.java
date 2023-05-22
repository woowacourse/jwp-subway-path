package subway.domain;

import java.util.Objects;

public class Fare {

    private static final int ADDITIONAL_MINIMUM_FARE = 2_050;
    private static final int MINIMUM_FARE = 1_250;

    private final int value;

    private Fare(final int value) {
        this.value = value;
    }

    public static Fare from(final int distance) {
        return new Fare(calculateFare(distance));
    }

    private static int calculateFare(final int distance) {
        if (distance <= 10) {
            return MINIMUM_FARE;
        }

        int additionalDistance = distance - 10;
        if (distance <= 50) {
            return MINIMUM_FARE + calculateOverFare(additionalDistance);
        }
        return ADDITIONAL_MINIMUM_FARE + calculateOverFare(additionalDistance);
    }

    private static int calculateOverFare(int additionalDistance) {
        if (additionalDistance <= 40) {
            return (int) ((Math.ceil((additionalDistance - 1) / 5) + 1) * 100);
        }
        additionalDistance -= 40;
        return (int) ((Math.ceil((additionalDistance - 1) / 8) + 1) * 100);
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
        return getValue() == fare.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
