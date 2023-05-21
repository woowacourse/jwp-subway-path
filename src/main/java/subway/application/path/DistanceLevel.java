package subway.application.path;

public enum DistanceLevel {
    LEVEL1(1, 10, 0),
    LEVEL2(11, 50, 5),
    LEVEL3(51, Integer.MAX_VALUE, 8);

    DistanceLevel(int minDistance, int maxDistance, int levelPerDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.levelPerDistance = levelPerDistance;
    }

    private final int minDistance;
    private final int maxDistance;
    private final int levelPerDistance;

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

    public int getMinDistance() {
        return minDistance;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public int getLevelPerDistance() {
        return levelPerDistance;
    }
}
