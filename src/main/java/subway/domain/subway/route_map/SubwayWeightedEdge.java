package subway.domain.subway.route_map;

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
