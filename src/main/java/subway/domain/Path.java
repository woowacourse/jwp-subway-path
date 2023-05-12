package subway.domain;

public final class Path {
    private final Station up;
    private final Station down;
    private final int distance;

    public Path(final Station up, final Station down, final int distance) {
        validate(distance);
        this.up = up;
        this.down = down;
        this.distance = distance;
    }

    private void validate(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 양의 정수여야 합니다.");
        }
    }
}
