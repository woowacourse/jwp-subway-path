package subway.domain;

import subway.exception.GlobalException;

public class Distance {
    private static final int MIN_DISTANCE = 1;
    private static final int MAX_DISTANCE = 10;

    private final int distance;

    public Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(int distance) {
        if (distance < MIN_DISTANCE || distance > MAX_DISTANCE) {
            throw new GlobalException("역간 거리는 10km이하 양의 정수만 가능합니다.");
        }
    }

    public Distance add(Distance otherDistance) {
        return new Distance(this.distance + otherDistance.distance);
    }

    public Distance subtract(Distance otherDistance) {
        return new Distance(this.distance - otherDistance.distance);
    }

    public boolean isBiggerThanOtherDistance(Distance otherDistance) {
        return this.distance > otherDistance.distance;
    }

    public int getDistance() {
        return distance;
    }
}
