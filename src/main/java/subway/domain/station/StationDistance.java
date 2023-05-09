package subway.domain.station;

public class StationDistance {
    private static final int MINIMUM_DISTANCE = 1;

    private final int distance;

    public StationDistance(final int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(final int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new IllegalArgumentException();
        }
    }
}
