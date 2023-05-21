package subway.domain.routestrategy;

import java.util.List;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;

public interface RouteStrategy {

    List<Station> findShortestRoute(List<Line> lines, Station start, Station end);

    Distance findShortestDistance(List<Line> lines, Station start, Station end);

}
