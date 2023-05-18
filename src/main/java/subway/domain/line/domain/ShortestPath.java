package subway.domain.line.domain;

import subway.domain.station.entity.StationEntity;

import java.util.List;

public class ShortestPath {
    private final List<StationEntity> path;
    private final double distance;

    public ShortestPath(final List<StationEntity> path, final double distance) {
        this.path = path;
        this.distance = distance;
    }

    public List<StationEntity> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }
}
