package subway.domain;

public class Section {

    private final Station up;
    private final Station down;
    private final int distance;

    public Section(final Station up, final Station down, final int distance) {
        validateDistance(distance);
        this.up = up;
        this.down = down;
        this.distance = distance;
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("역간 거리는 양수여야 합니다.");
        }
    }
}
