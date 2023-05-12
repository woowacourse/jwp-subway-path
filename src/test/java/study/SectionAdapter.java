package study;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Section;
import subway.domain.Station;

public class SectionAdapter extends DefaultWeightedEdge {

    public Section toSection() {
        return new Section(getSource(), getTarget(), weight());
    }

    @Override
    protected Station getSource() {
        return (Station) super.getSource();
    }

    @Override
    protected Station getTarget() {
        return (Station) super.getTarget();
    }

    public int weight() {
        return (int) super.getWeight();
    }
}
