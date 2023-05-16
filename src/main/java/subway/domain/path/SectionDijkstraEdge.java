package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Station;
import subway.domain.section.Section;

public class SectionDijkstraEdge extends DefaultWeightedEdge {

    private final Section section;

    public SectionDijkstraEdge(final Section section) {
        this.section = section;
    }

    protected Station getSource() {
        return section.getPrevStation();
    }

    protected Station getTarget() {
        return section.getNextStation();
    }

    @Override
    protected double getWeight() {
        return section.getDistance().getValue();
    }

    public Section getSection() {
        return section;
    }
}
