package subway.domain.station;

public class StationDistance {
    private static final int MINIMUM_DISTANCE = 1;

    private final int distance;

    public StationDistance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new IllegalArgumentException();
        }
    }
}
