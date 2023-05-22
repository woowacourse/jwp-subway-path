package subway.domain.route;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.line.Line;

public class SectionEdge extends DefaultWeightedEdge {

    private final Line line;

    public SectionEdge(Line line) {
        super();
        this.line = line;
    }

    public Line getLine() {
        return line;
    }
}
