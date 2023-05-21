package subway.domain;

import org.jgrapht.graph.DefaultEdge;

public class StationEdge extends DefaultEdge {

    private final Distance distance;
    private final Line line;

    public StationEdge(final Distance distance, final Line line) {
        this.distance = distance;
        this.line = line;
    }

    public Distance getDistance() {
        return distance;
    }

    public Line getLine() {
        return line;
    }
}
