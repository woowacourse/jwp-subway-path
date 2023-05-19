package subway.application.path;

public enum PriceLevel {
    LEVEL1,
    LEVEL2,
    LEVEL3;

    public static PriceLevel from(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 1 이상의 값이어야 합니다.");
        }
        if (distance < 10) {
            return LEVEL1;
        }
        if (distance <= 50) {
            return LEVEL2;
        }
        return LEVEL3;
    }
}
