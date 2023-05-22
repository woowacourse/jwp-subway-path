package subway.domain.routestrategy;

import java.util.List;
import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.Subway;

public interface RouteStrategy {

    List<Station> findShortestRoute(Subway subway, Station start, Station end);

    Distance findShortestDistance(Subway subway, Station start, Station end);

}
