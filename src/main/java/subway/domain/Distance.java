package subway.domain;

import subway.exception.InValidDistanceException;

public class Distance {
    private static final int MIN_DISTANCE = 1;
    private static final int MAX_DISTANCE = 100;

    private final int distance;

    public Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(int distance) {
        if (distance < MIN_DISTANCE || distance > MAX_DISTANCE) {
            throw new InValidDistanceException();
        }
    }

    public int getDistance() {
        return distance;
    }

    public boolean isBiggerThanOtherDistance(Distance otherDistance) {
        return this.distance > otherDistance.distance;
    }

    public Distance subtract(Distance otherDistance) {
        return new Distance(this.distance - otherDistance.distance);
    }

    public Distance sum(Distance otherDistance) {
        return new Distance(this.distance + otherDistance.distance);
    }
}
