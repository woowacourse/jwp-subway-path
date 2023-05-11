package subway.service.section.domain;

import java.util.Objects;

public class Distance {
    private final int distance;

    public Distance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("지하철 역 간 거리는 양의 정수만 가능합니다.");
        }
        this.distance = distance;
    }

    public Distance reduce(Distance another) {
        return new Distance(this.distance - another.distance);
    }

    public Distance plus(Distance another) {
        return new Distance(this.distance + another.distance);
    }

    public boolean isSmaller(Distance another) {

        return this.distance <= another.distance;
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

    @Override
    public String toString() {
        return "Distance{" +
                "distance=" + distance +
                '}';
    }
}
