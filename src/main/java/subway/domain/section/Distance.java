package subway.domain.section;

public class Distance {

    private static final int MINIMAL_VALUE = 1;
    private static final int MAXIMUM_VALUE = 100;

    private final int distance;

    private Distance(final int distance) {
        validate(distance);

        this.distance = distance;
    }

    public static Distance from(final int distance) {
        return new Distance(distance);
    }

    public void validate(final int distance) {
        if (!(MINIMAL_VALUE <= distance && distance <= MAXIMUM_VALUE)) {
            throw new IllegalArgumentException("길이는 1 이상, 100 이하만 가능합니다.");
        }
    }

    public Distance plus(final Distance other) {
        return new Distance(this.distance + other.distance);
    }

    public Distance minus(final Distance other) {
        return new Distance(this.distance - other.distance);
    }

    public boolean isGreaterThanOrEqualTo(final Distance target) {
        return this.distance >= target.distance;
    }

    public boolean isLessThan(final Distance target) {
        return this.distance < target.distance;
    }

    public int distance() {
        return distance;
    }

    public int getDistance() {
        return distance;
    }
}
