package subway.business.domain;

import java.util.Objects;

public class Distance {
    private final int distance;
    private static final int MIN_LENGTH = 1;

    public Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    public void validate(int distance) {
        if (distance < MIN_LENGTH) {
            throw new IllegalArgumentException("거리는 1보다 작을 수 없습니다.");
        }
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
