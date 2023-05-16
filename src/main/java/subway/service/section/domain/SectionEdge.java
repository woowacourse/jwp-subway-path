package subway.service.section.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Distance distance;

    public SectionEdge(Distance distance) {
        this.distance = distance;
    }

    @Override
    protected double getWeight() {
        return distance.getDistance();
    }
}
