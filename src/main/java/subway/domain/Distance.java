package subway.domain;

import subway.exception.GlobalException;

public class Distance {
    private static final int MIN_DISTANCE = 1;
    private static final int MAX_DISTANCE = 10;

    private final int distance;

    public Distance(final int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(final int distance) {
        if (distance < MIN_DISTANCE || distance > MAX_DISTANCE) {
            throw new GlobalException("역간 거리는 10km이하 양의 정수만 가능합니다.");
        }
    }

    public int getDistance() {
        return distance;
    }

    public boolean isBiggerThanOtherDistance(final Distance otherDistance) {
        return this.distance > otherDistance.distance;
    }

    public Distance subtract(final Distance otherDistance) {
        return new Distance(this.distance - otherDistance.distance);
    }
}
