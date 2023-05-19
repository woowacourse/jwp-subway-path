package subway.domain.vo;

import java.util.Objects;

public class Distance {

    private final int distance;

    public Distance(final int distance) {
        validatePositive(distance);
        this.distance = distance;
    }

    private static void validatePositive(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 항상 양의 정수여야 합니다.");
        }
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
