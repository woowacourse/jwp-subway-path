package subway.domain.section;

import subway.exception.distance.InvalidDistanceException;

public class Distance {

    private final int distance;

    public Distance(final int distance) {
        validateDistanceIsPositive(distance);
        this.distance = distance;
    }

    private void validateDistanceIsPositive(final int distance) {
        if (distance <= 0) {
            throw new InvalidDistanceException();
        }
    }

    public int getDistance() {
        return distance;
    }
}
