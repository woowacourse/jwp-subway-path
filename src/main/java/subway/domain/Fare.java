package subway.domain;

import java.util.Objects;

public class Fare {

    private static final int BASE_FARE = 1250;
    private static final int SURCHARGE_FARE = 100;
    private static final int FIRST_STEP_DISTANCE = 10;
    private static final int SECOND_STEP_DISTANCE = 50;

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(final Distance distance) {
        return new Fare(calculateFee(distance.getDistance()));
    }

    private static int calculateFee(final int distance) {
        if (distance > SECOND_STEP_DISTANCE) {
            return getSecondStepFare(distance);
        }
        if (distance > FIRST_STEP_DISTANCE) {
            return getFirstStepFare(distance);
        }
        return BASE_FARE;
    }

    private static int getFirstStepFare(int distance) {
        int additionalFare = ((distance - FIRST_STEP_DISTANCE) / 5) * SURCHARGE_FARE;
        if ((distance - FIRST_STEP_DISTANCE) % 5 != 0) {
            additionalFare += SURCHARGE_FARE;
        }
        return BASE_FARE + additionalFare;
    }

    private static int getSecondStepFare(int distance) {
        int additionalFare = (8 + (distance - SECOND_STEP_DISTANCE) / 8) * SURCHARGE_FARE;
        if ((distance - SECOND_STEP_DISTANCE) % 8 != 0) {
            additionalFare += SURCHARGE_FARE;
        }
        return BASE_FARE + additionalFare;
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

}
