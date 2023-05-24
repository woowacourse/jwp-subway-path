package subway.domain.path;

import java.util.List;
import subway.domain.line.Line;
import subway.domain.station.Station;

public interface SubwayPathFinder {

    SubwayPath findShortestPath(List<Line> allLines, Station from, Station to);
}
