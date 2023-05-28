package subway.domain.fare;

import subway.domain.Line;
import subway.domain.route.Route;
import subway.domain.route.RouteEdge;
import subway.domain.vo.Distance;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class LineDistance {

    private final Map<Line, Distance> lineTotalDistance;

    private LineDistance(final Map<Line, Distance> lineTotalDistance) {
        this.lineTotalDistance = lineTotalDistance;
    }

    public static LineDistance from(final Route route) {
        return new LineDistance(collectLineDistance(route));
    }

    private static Map<Line, Distance> collectLineDistance(final Route route) {
        return route.getRouteEdges()
                .stream()
                .collect(groupingBy(RouteEdge::getLine, HashMap::new,
                        mapping(RouteEdge::getDistance, reducing(Distance.ZERO, Distance::plus))));
    }

    public Map<Line, Distance> getLineTotalDistance() {
        return new HashMap<>(lineTotalDistance);
    }
}
