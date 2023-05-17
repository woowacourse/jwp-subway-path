package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionProxy extends DefaultWeightedEdge {

    private final Section section;

    private SectionProxy(Section section) {
        this.section = section;
    }

    public static SectionProxy from(Section section) {
        return new SectionProxy(section);
    }

    public Section toSection() {
        return new Section(getSource(), getTarget(), (int) getWeight());
    }

    @Override
    public Station getSource() {
        return section.getUp();
    }

    @Override
    public Station getTarget() {
        return section.getDown();
    }

    @Override
    public double getWeight() {
        return section.getDistance();
    }
}
