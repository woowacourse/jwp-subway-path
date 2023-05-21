package subway.domain;

public class FareCalculator {
    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int DISTANCE_5KM = 5;
    private static final int LONG_DISTANCE = 50;
    private static final int DISTANCE_8KM = 8;
    private static final int PRICE_100 = 100;

    public int calculate(int distance) {
        validateDistance(distance);

        if (distance <= DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }

        if (DEFAULT_DISTANCE < distance && distance <= LONG_DISTANCE) {
            return calculateMidDistance(distance);
        }

        if (distance > LONG_DISTANCE) {
            return calculateLongDistance(distance);
        }

        throw new IllegalArgumentException("요금을 측정 할 수 없습니다.");
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 양의 정수 이어야 합니다.");
        }
    }

    private int calculateMidDistance(int distance) {
        int remainDistance = distance - DEFAULT_DISTANCE;
        int additionalFare = calculateOverFare(remainDistance, DISTANCE_5KM, PRICE_100);
        return DEFAULT_FARE + additionalFare;
    }

    private int calculateOverFare(int distance, int perExceedingDistance, int additionalFare) {
        return (int) ((Math.ceil((distance - 1) / perExceedingDistance) + 1) * additionalFare);
    }

    private int calculateLongDistance(int distance) {
        int remainDistance = distance - LONG_DISTANCE;
        int additionalMidFare = ((LONG_DISTANCE - DEFAULT_DISTANCE) / DISTANCE_5KM) * PRICE_100;
        int additionalFare = calculateOverFare(remainDistance, DISTANCE_8KM, PRICE_100);
        return DEFAULT_FARE + additionalMidFare + additionalFare;
    }


}

