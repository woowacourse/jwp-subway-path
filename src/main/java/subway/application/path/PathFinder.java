package subway.application.path;


import subway.domain.MultiLineSections;
import subway.domain.ShortestPath;
import subway.domain.Station;

public interface PathFinder {

    ShortestPath findShortestPath(MultiLineSections sections, Station upStationId, Station downStationId);
}
