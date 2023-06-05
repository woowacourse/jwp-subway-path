package subway.route.domain;

import java.util.List;

public class Edges {

    private final List<InterStationEdge> edges;

    public Edges(List<InterStationEdge> edges) {
        this.edges = edges;
    }

    public List<InterStationEdge> getEdges() {
        return edges;
    }
}
