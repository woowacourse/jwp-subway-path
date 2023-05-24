package subway.application.path;


import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.domain.section.MultiLineSections;

public interface PathFinder {

    ShortestPath findShortestPath(MultiLineSections sections, Station upStationId, Station downStationId);
}
