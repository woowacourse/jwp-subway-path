package subway.vo;

import java.util.Objects;

public class Distance {

    private final int distance;

    private Distance(final int distance) {
        this.distance = distance;
    }

    public static Distance from(final int distance) {
        return new Distance(distance);
    }

    public int getValue() {
        return distance;
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
}
