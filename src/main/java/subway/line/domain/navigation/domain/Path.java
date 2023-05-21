package subway.line.domain.navigation.domain;

import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

import java.util.List;

public interface Path {

    List<Station> findShortestPath(Station stationA, Station stationB);

    Distance findShortestDistance(Station stationA, Station stationB);
}
