package subway.domain;

public enum FareCalculator {

    FIRST_DISTANCE_FARE(10, 1, 0),
    SECOND_DISTANCE_FARE(50, 5, 100),
    THIRD_DISTANCE_FARE(Integer.MAX_VALUE, 8, 100);

    private static final int DEFAULT_FARE = 1_250;

    private final int distance;
    private final int additionalDistance;
    private final int additionalFare;

    FareCalculator(final int distance, final int additionalDistance, final int additionalFare) {
        this.distance = distance;
        this.additionalDistance = additionalDistance;
        this.additionalFare = additionalFare;
    }

    public static int calculate(double distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리 값은 음수일 수 없습니다. 유효한 거리 값을 입력해주세요.");
        }

        int additionalFare = 0;
        int startDistance = 0;

        for (FareCalculator value : FareCalculator.values()) {
            for (; startDistance <= value.distance; startDistance += value.additionalDistance) {
                if (startDistance > distance) {
                    return DEFAULT_FARE + additionalFare;
                }
                additionalFare += value.additionalFare;
            }
        }
        return DEFAULT_FARE + additionalFare;
    }
}
