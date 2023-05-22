package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.entity.Station;

public class SectionEdge extends DefaultWeightedEdge {

    @Override
    public Station getSource() {
        return (Station) super.getSource();
    }

    @Override
    public Station getTarget() {
        return (Station) super.getTarget();
    }

    @Override
    public double getWeight() {
        return super.getWeight();
    }
}
