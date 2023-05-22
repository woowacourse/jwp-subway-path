package subway.domain.path;

import subway.exception.distance.InvalidDistanceException;

public class Distance {

    private final int distance;

    public Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(final int distance) {
        if (distance < 0) {
            throw new InvalidDistanceException();
        }
    }

    public int calculateOverFare(final int standard) {
        return distance - standard;
    }


    public boolean isLowerOrEqual(final int standard) {
        return distance <= standard;
    }

    public int getValue() {
        return distance;
    }
}
