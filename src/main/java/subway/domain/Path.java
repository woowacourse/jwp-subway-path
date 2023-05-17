package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Path extends DefaultWeightedEdge {
    private final Section section;

    public Path(Section section) {
        this.section = section;
    }

    @Override
    protected double getWeight() {
        return section.getDistance().getValue();
    }

    @Override
    protected Station getSource() {
        return section.getUp();
    }

    @Override
    protected Station getTarget() {
        return section.getDown();
    }

}
