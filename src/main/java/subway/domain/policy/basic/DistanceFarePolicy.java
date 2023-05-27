package subway.domain.policy.basic;

import subway.domain.line.Lines;

public class DistanceFarePolicy implements FarePolicy {

    private static final int DEFAULT_FARE = 1250;
    private static final int FIRST_DISTANCE_CRITERION = 10;
    private static final int SECOND_DISTANCE_CRITERION = 50;
    private static final int FIRST_EXTRA_DISTANCE_UNIT = 5;
    private static final int SECOND_EXTRA_DISTANCE_UNIT = 8;
    private static final int EXTRA_FARE_UNIT = 100;

    private DistanceFarePolicy() {
    }

    public static DistanceFarePolicy getInstance() {
        return new DistanceFarePolicy();
    }

    @Override
    public int calculateFare(int distance, Lines lines) {
        validateNegativeDistance(distance);

        int mostExpensiveExtraFare = lines.findMostExpensiveExtraFare();

        if (distance <= FIRST_DISTANCE_CRITERION) {
            return DEFAULT_FARE + mostExpensiveExtraFare;
        }

        return DEFAULT_FARE + calculateOverFare(distance) + mostExpensiveExtraFare;
    }

    private void validateNegativeDistance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException(
                    String.format("[ERROR] 요금 계산 상 거리는 음수가 될 수 없습니다. (입력값 : %d)", distance)
            );
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
