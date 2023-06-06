package subway.domain.pathfinder;

import java.util.Map;
import subway.domain.subwaymap.Line;
import subway.domain.subwaymap.Sections;
import subway.domain.subwaymap.Station;

public interface SubwayMapShortestPathFinder {

    ShortestPath getShortestPath(final Map<Line, Sections> linesAndSections, final Station start, final Station end);
}
