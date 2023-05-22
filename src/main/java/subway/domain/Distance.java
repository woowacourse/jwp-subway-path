package subway.domain;

import java.util.Objects;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

    public int getDistance() {
        return distance;
    }
}
