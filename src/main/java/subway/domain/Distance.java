package subway.domain;

import java.util.Objects;

public class Distance {

    //todo :  null - 종점역 과 같은 경우에는 distance를 null로 해야하나 0으로 해야하나
    private final Integer distance;

    private Distance(Integer distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    public static Distance createEmpty() {
        return new Distance(null);
    }


    public static Distance from(Integer distance) {
        return new Distance(distance);
    }

    private void validateDistance(Integer distance) {
        if (distance == null) {
            return;
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("[ERROR] 거리는 양의 정수만 가능합니다.");
        }
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return Objects.equals(distance, distance1.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

    public boolean isEmpty() {
        return this.distance == null;
    }
}
