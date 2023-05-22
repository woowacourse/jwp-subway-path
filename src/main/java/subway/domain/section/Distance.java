package subway.domain.section;

public class Distance {

    private static final int MIN_DISTANCE = 1;
    private final int distance;

    public Distance(final int distance) {
        validatePositive(distance);
        this.distance = distance;
    }

    private void validatePositive(final int distance) {
        if (MIN_DISTANCE > distance) {
            throw new IllegalArgumentException("길이는 1 이상이어야합니다.");
        }
    }

    public Distance subtract(final Distance other) {
        return new Distance(this.distance - other.distance);
    }

    public Distance add(final Distance other) {
        return new Distance(this.distance + other.distance);
    }

    public boolean isLongerThan(final Distance other) {
        return this.distance > other.distance;
    }

    public int distance() {
        return distance;
    }
}
