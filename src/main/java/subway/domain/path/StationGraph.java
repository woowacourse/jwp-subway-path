package subway.domain.path;

import subway.domain.station.Station;

public interface StationGraph {
    ShortestPath getShortestPath(Station startStation, Station endStation);
}
