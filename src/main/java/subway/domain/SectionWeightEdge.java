package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.section.Section;

public class SectionWeightEdge extends DefaultWeightedEdge {

    @Override
    public Station getSource() {
        return (Station) super.getSource();
    }

    @Override
    public Station getTarget() {
        return (Station) super.getTarget();
    }

    public int getDistance() {
        return (int) super.getWeight();
    }

    public Section toSection() {
        return Section.of(getSource(), getTarget(), Distance.from(getDistance()));
    }
}
