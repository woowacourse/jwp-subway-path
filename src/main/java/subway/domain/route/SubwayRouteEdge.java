package subway.domain.route;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Line;

public class SubwayRouteEdge extends DefaultWeightedEdge {
    private final Line line;

    public SubwayRouteEdge(final Line line) {
        this.line = line;
    }
}
