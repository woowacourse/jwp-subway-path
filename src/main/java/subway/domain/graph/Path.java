package subway.domain.graph;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Section;
import subway.domain.Station;

public class Path extends DefaultWeightedEdge {
    private final Section section;

    public Path(final Section section) {
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
