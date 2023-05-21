package subway.application.path;

public enum DistanceLevel {
    LEVEL1(10),
    LEVEL2(50),
    LEVEL3(Integer.MAX_VALUE);

    DistanceLevel(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    private final int maxDistance;

    public static DistanceLevel from(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 1 이상의 값이어야 합니다.");
        }
        if (distance <= LEVEL1.maxDistance) {
            return LEVEL1;
        }
        if (distance <= LEVEL2.maxDistance) {
            return LEVEL2;
        }
        return LEVEL3;
    }
}
