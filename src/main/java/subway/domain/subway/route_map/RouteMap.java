package subway.domain.subway.route_map;

import java.util.List;
import subway.domain.Path;
import subway.domain.line.Line;
import subway.domain.station.Station;

public interface RouteMap {

    void update(List<Line> lines);

    Path findShortestPath(Station source, Station destination);
}
