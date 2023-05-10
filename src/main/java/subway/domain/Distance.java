package subway.domain;

public class Distance {

    private final int distance;

    public Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("[ERROR] 거리는 양의 정수여야 합니다.");
        }
    }

    public int getDistance() {
        return distance;
    }
}
