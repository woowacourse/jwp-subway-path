package subway.domain.general;

import subway.exception.IllegalDistanceException;

public class Distance {
    public static final int MINIMAL_DISTANCE = 0;

    private final double distance;

    private Distance(double distance) {
        validate(distance);
        this.distance = distance;
    }

    public static Distance of(double distance) {
        return new Distance(distance);
    }

    private void validate(double distance) {
        if (distance < MINIMAL_DISTANCE) {
            throw new IllegalDistanceException("유효하지 않은 거리입니다. 0이상의 정수이어야 합니다.");
        }
    }
    public double getDistance() {
        return distance;
    }

    public boolean isSameOrOver(int other) {
        return this.distance >= other;
    }

    public boolean isOver(int other) {
        return this.distance > other;
    }
}
