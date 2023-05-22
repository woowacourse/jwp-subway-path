package subway.domain;

import java.util.Objects;

public class Fare {

    private static final int DEFAULT_FARE = 1_250;
    private static final int DEFAULT_FARE_OVER_50 = 2_050;
    private static final int ADDITIONAL_FARE = 100;

    private static final int MINIMUM_DISTANCE = 1;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int ADDITIONAL_DISTANCE = 50;

    private static final int DISTANCE_UNIT_BETWEEN_10_TO_50 = 5;
    private static final int DISTANCE_UNIT_OVER_50 = 8;


    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare createByDistance(int distance) {
        return new Fare(calculate(distance));
    }

    private static int calculate(int distance) {
        if (MINIMUM_DISTANCE <= distance && distance < DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }
        if (DEFAULT_DISTANCE <= distance && distance < ADDITIONAL_DISTANCE) {
            int additionalDistance = distance - DEFAULT_DISTANCE;
            return DEFAULT_FARE + getAdditionalFareByUnity(additionalDistance, DISTANCE_UNIT_BETWEEN_10_TO_50);
        }
        if (ADDITIONAL_DISTANCE <= distance) {
            int additionalDistance = distance - ADDITIONAL_DISTANCE;
            return DEFAULT_FARE_OVER_50 + getAdditionalFareByUnity(additionalDistance, DISTANCE_UNIT_OVER_50);
        }
        throw new IllegalArgumentException("요금을 계산할 수 없는 거리입니다.");
    }

    private static int getAdditionalFareByUnity(int distance, int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * ADDITIONAL_FARE);
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "fare=" + fare +
                '}';
    }
}
