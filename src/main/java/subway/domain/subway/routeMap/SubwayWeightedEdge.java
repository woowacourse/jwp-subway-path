package subway.domain.subway.routeMap;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.line.Line;

public final class SubwayWeightedEdge extends DefaultWeightedEdge {

    private Line line;

    public void setLine(final Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }
}
