package subway.domain.routestrategy;
import java.util.List;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;

public class DijkstraRouteStrategy implements RouteStrategy {

    @Override
    public List<Station> findShortestRoute(List<Line> lines, Station start, Station end) {
        return null;
    }

    @Override
    public Distance findShortestDistance(List<Line> lines, Station start, Station end) {
        return null;
    }
}
