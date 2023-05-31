package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;

    public SectionEdge(final Section section) {
        this.section = section;
    }

    public Station getSourceVertex() {
        return section.getUpper();
    }

    public Station getTargetVertex() {
        return section.getLower();
    }

    public Long getLineId() {
        return section.getLindId();
    }

    public Station getUpper() {
        return section.getUpper();
    }

    public Station getLower() {
        return section.getLower();
    }

    public Distance getDistance() {
        return section.getDistance();
    }

    @Override
    public double getWeight() {
        return section.getDistance().getValue();
    }
}
