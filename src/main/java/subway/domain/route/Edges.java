package subway.domain.route;

import java.util.List;

public class Edges {

    private final List<InterStationEdge> edges;

    public Edges(final List<InterStationEdge> edges) {
        this.edges = edges;
    }

    public List<InterStationEdge> getEdges() {
        return edges;
    }
}
