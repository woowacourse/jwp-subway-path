package subway.domain.fare;

import java.util.Objects;

public class SubwayFare {

    private static final int DEFAULT_FARE = 1250;
    private static final int FIRST_RANGE_ADDITIONAL_FARE = 800;
    private static final int ADDITIONAL_FARE = 100;

    private static final int MIN_CALCULABLE_DISTANCE = 1;
    private static final int FIRST_RANGE_START_DISTANCE = 10;
    private static final int SECOND_RANGE_START_DISTANCE = 50;
    private static final int FIRST_RANGE_ADDITIONAL_FARE_CRITERIA_DISTANCE = 5;
    private static final int SECOND_RANGE_ADDITIONAL_FARE_CRITERIA_DISTANCE = 8;
    private final int fare;

    private SubwayFare(int fare) {
        this.fare = fare;
    }

    public static SubwayFare generateFareByDistance(int distance) {
        if (distance >= MIN_CALCULABLE_DISTANCE && distance <= FIRST_RANGE_START_DISTANCE) {
            return new SubwayFare(DEFAULT_FARE);
        }
        if (distance > FIRST_RANGE_START_DISTANCE && distance <= SECOND_RANGE_START_DISTANCE) {
            return getFirstRangeAdditionalFare(distance);
        }
        if (distance > SECOND_RANGE_ADDITIONAL_FARE_CRITERIA_DISTANCE) {
            return getSecondRangeAdditionalFare(distance);
        }
        throw new IllegalStateException("요금을 계산할 수 없는 거리입니다.");
    }

    private static SubwayFare getFirstRangeAdditionalFare(int distance) {
        int additionalDistance = distance - FIRST_RANGE_START_DISTANCE;
        int additionalFare = (int) ((Math.ceil((additionalDistance - 1) / FIRST_RANGE_ADDITIONAL_FARE_CRITERIA_DISTANCE) + 1) * ADDITIONAL_FARE);
        return new SubwayFare(DEFAULT_FARE + additionalFare);
    }

    private static SubwayFare getSecondRangeAdditionalFare(int distance) {
        int additionalDistance = distance - SECOND_RANGE_START_DISTANCE;
        int additionalFare = (int) ((Math.ceil((additionalDistance - 1) / SECOND_RANGE_ADDITIONAL_FARE_CRITERIA_DISTANCE) + 1) * ADDITIONAL_FARE);
        return new SubwayFare(DEFAULT_FARE + FIRST_RANGE_ADDITIONAL_FARE + additionalFare);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubwayFare subwayFare1 = (SubwayFare) o;
        return fare == subwayFare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    public int getFare() {
        return fare;
    }

    @Override
    public String toString() {
        return "Fare{" +
                "fare=" + fare +
                '}';
    }
}
