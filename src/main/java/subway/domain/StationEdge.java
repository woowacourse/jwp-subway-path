package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class StationEdge extends DefaultWeightedEdge {

    private Line line;

    public StationEdge(final Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }
}
