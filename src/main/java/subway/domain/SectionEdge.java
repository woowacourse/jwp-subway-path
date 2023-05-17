package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    public Section toSection() {
        return Section.of(getSource(), getTarget(), getDistance());
    }

    @Override
    protected Station getSource() {
        return (Station) super.getSource();
    }

    @Override
    protected Station getTarget() {
        return (Station) super.getTarget();
    }

    private int getDistance() {
        return (int) super.getWeight();
    }

}
