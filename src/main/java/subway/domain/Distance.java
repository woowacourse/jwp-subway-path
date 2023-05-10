package subway.domain;

public class Distance {

    private final int distance;

    public Distance(final int distance) {
        validate(distance);
        this.distance = distance;
    }

    private static void validate(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 양수여야 합니다.");
        }
    }
}
