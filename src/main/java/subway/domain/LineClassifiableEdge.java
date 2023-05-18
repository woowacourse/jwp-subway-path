package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.entity.Line;

public class LineClassifiableEdge extends DefaultWeightedEdge {

    private Line line;

    public LineClassifiableEdge(final Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }
}
