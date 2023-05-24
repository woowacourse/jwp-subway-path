package subway.domain.path;

import subway.domain.station.Station;

import java.util.List;

public final class DijkstraShortestPath implements ShortestPath{

    private final List<Station> path;
    private final int distance;

    public DijkstraShortestPath(List<Station> stations, int distance) {
        this.path = stations;
        this.distance = distance;
    }

    @Override
    public List<Station> getPath() {
        return path;
    }

    @Override
    public int getDistance() {
        return distance;
    }
}
