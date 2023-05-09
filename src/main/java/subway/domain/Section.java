package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Section extends DefaultWeightedEdge {
    private final Station source;
    private final Station target;
    private final int distance;
    private final Line line;

    public Section(final Station source, final Station target, final int distance, final Line line) {
        this.source = source;
        this.target = target;
        this.distance = distance;
        this.line = line;
    }
}
