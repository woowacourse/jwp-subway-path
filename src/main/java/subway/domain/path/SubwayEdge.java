package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayEdge extends DefaultWeightedEdge {

    private SubwayEdge() {
        super();
    }

    public int getDistance() {
        return (int) super.getWeight();
    }
}
