package subway.domain.section;

import java.util.Objects;

public class Distance {

    private final int distance;

    private Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    private void validateDistance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException(
                    String.format("[ERROR] 거리는 음수 일 수 없습니다. (입력값 : %d)", distance)
            );
        }
    }

    public Distance add(Distance distance) {
        return new Distance(this.distance + distance.distance);
    }

    public Distance subtract(Distance newDistance) {
        return new Distance(distance - newDistance.distance);
    }

    public boolean isGreaterThan(Distance other) {
        return distance > other.distance;
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
