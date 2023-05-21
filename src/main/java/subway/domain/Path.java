package subway.domain;

import subway.entity.StationEntity;

import java.util.List;

public class Path {

    private final List<StationEntity> path;
    private final double distance;
    private final int fare;

    public Path(final List<StationEntity> path, final double distance) {
        this.path = path;
        this.distance = distance;
        this.fare = FareCalculator.calculate(distance);
    }

    public List<StationEntity> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
