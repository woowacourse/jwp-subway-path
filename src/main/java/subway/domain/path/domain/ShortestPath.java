package subway.domain.path.domain;

import subway.domain.station.entity.StationEntity;

import java.util.List;

public class ShortestPath {

    private List<StationEntity> path;
    private double distance;
    private int fare;

    public ShortestPath() {
    }

    public ShortestPath(final List<StationEntity> path, final double distance) {
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
