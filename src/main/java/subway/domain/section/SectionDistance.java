package subway.domain.section;

public class SectionDistance {

    private static final int MIN_DISTANCE = 1;
    private static final int MAX_DISTANCE = 50;

    private final int distance;

    public SectionDistance(final int distance) {
        validateRange(distance);
        this.distance = distance;
    }

    private void validateRange(final int distance) {
        if (distance < MIN_DISTANCE || distance > MAX_DISTANCE) {
            throw new IllegalArgumentException(String.format("거리는 최소 %d부터 최대 %d까지 가능합니다.", MIN_DISTANCE, MAX_DISTANCE));
        }
    }

    public int getDistance() {
        return distance;
    }
}
