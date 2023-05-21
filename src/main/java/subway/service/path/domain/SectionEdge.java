package subway.service.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.service.section.domain.Distance;

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
