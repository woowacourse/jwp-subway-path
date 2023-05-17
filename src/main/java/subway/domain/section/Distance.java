package subway.domain.section;

public class Distance {

    private final int distance;

    public Distance(final int distance) {
        validateDistanceIsPositive(distance);
        this.distance = distance;
    }

    private void validateDistanceIsPositive(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 자연수여야 합니다.");
        }
    }

    public int getDistance() {
        return distance;
    }
}
