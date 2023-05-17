package subway.util;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Station;

public class SectionWeightEdge extends DefaultWeightedEdge {

    @Override
    protected Station getSource() {
        return (Station) super.getSource();
    }

    @Override
    protected Station getTarget() {
        return (Station) super.getTarget();
    }

    public int getDistance() {
        return (int) super.getWeight();
    }
}
