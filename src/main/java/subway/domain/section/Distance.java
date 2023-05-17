package subway.domain.section;

public class Distance {
    private static final int MAX_DISTANCE = 100;
    private final int distance;

    public Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("구간의 길이는 1 이상 이어야 합니다.");
        }
        if (distance > MAX_DISTANCE) {
            throw new IllegalArgumentException("구간의 길이는 " + MAX_DISTANCE + " 이하여야 합니다.");
        }
    }

    public int getDistance() {
        return distance;
    }
}
