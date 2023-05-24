package subway.domain.path;

import subway.domain.station.Station;

import java.util.List;

public interface ShortestPath {
    List<Station> getPath();
    int getDistance();
}
