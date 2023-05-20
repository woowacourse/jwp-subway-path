package subway.domain.fare;

public class DistanceFarePolicy implements FarePolicy {

    public static final int DEFAULT_FARE = 1250;
    public static final int FIRST_DISTANCE_CRITERION = 10;
    public static final int SECOND_DISTANCE_CRITERION = 50;
    public static final int FIRST_EXTRA_DISTANCE_UNIT = 5;
    public static final int SECOND_EXTRA_DISTANCE_UNIT = 8;
    public static final int EXTRA_FARE_UNIT = 100;

    private DistanceFarePolicy() {
    }

    public static DistanceFarePolicy getInstance() {
        return new DistanceFarePolicy();
    }

    @Override
    public int calculateFare(int distance) {
        validateNegativeDistance(distance);

        if (distance <= FIRST_DISTANCE_CRITERION) {
            return DEFAULT_FARE;
        }

        return DEFAULT_FARE + calculateOverFare(distance);
    }

    private void validateNegativeDistance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("[ERROR] 요금 계산 상 거리는 음수가 될 수 없습니다.");
        }
    }

    private int calculateOverFare(int distance) {
        if (distance <= SECOND_DISTANCE_CRITERION) {
            int leftDistance = distance - FIRST_DISTANCE_CRITERION;
            return calculateExtraFareUnder50km(leftDistance);
        }

        return calculateExtraFareUnder50km(SECOND_DISTANCE_CRITERION - FIRST_DISTANCE_CRITERION)
                + calculateOverFareOver50km(distance - SECOND_DISTANCE_CRITERION);
    }

    private int calculateExtraFareUnder50km(int leftDistance) {
        if (leftDistance > 0) {
            return (int) ((Math.ceil((leftDistance - 1) / FIRST_EXTRA_DISTANCE_UNIT) + 1) * EXTRA_FARE_UNIT);
        }
        return 0;
    }

    private int calculateOverFareOver50km(int leftDistance) {
        if (leftDistance > 0) {
            return (int) ((Math.ceil((leftDistance - 1) / SECOND_EXTRA_DISTANCE_UNIT) + 1) * EXTRA_FARE_UNIT);
        }
        return 0;
    }

}
