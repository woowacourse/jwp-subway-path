package subway.domain;

public class Distance {

    private final int distance;

    public Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("역 간의 거리는 양수여야 합니다.");
        }
    }

    public boolean isBiggerThan(Distance targetDistance) {
        return distance > targetDistance.distance;
    }

    public Distance add(Distance targetDistance) {
        return new Distance(distance + targetDistance.distance);
    }

    public Distance sub(Distance targetDistance) {
        return new Distance(distance - targetDistance.distance);
    }
}
