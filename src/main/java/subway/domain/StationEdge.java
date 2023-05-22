package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class StationEdge extends DefaultWeightedEdge {
    private final Integer cost;

    public StationEdge(Integer cost) {
        this.cost = cost;
    }

    public Integer getCost() {
        return cost;
    }
}
