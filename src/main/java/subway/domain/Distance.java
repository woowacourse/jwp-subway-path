package subway.domain;

public class Distance {

    private static final int MINIMAL_VALUE = 1;
    private static final int MAXIMUM_VALUE = 1_000_000;

    private final int distance;

    private Distance(final int distance) {
        validate(distance);

        this.distance = distance;
    }

    public static Distance from(final int distance) {
        return new Distance(distance);
    }

    public static Distance from(final Double distance) {
        return new Distance(distance.intValue());
    }

    public void validate(final int distance) {
        if (!(MINIMAL_VALUE <= distance && distance <= MAXIMUM_VALUE)) {
            throw new IllegalArgumentException("길이는 1 이상, 1,000,000 이하만 가능합니다.");
        }
    }

    public Distance add(final Distance other) {
        return new Distance(this.distance + other.distance);
    }

    public Distance minus(final Distance other) {
        return new Distance(this.distance - other.distance);
    }

    public boolean isLessThanOrEqualTo(final Distance value) {
        return distance <= value.getDistance();
    }

    public int getDistance() {
        return distance;
    }
}
