package subway.station.domain;

import java.util.Objects;

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

    public StationDistance subtract(final StationDistance other) {
        return new StationDistance(this.distance - other.distance);
    }

    public StationDistance sum(final StationDistance other) {
        return new StationDistance(this.distance + other.distance);
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StationDistance that = (StationDistance) o;
        return distance == that.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

}
