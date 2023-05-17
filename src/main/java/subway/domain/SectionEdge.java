package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    public Station getUpwardStation() {
        return (Station) super.getSource();
    }

    public Station getDownwardStation() {
        return (Station) super.getTarget();
    }

    public int getDistance() {
        return (int) super.getWeight();
    }
}
