package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.core.Section;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;
    private final int surcharge;

    public SectionEdge(final Section section, int amount) {
        this.section = section;
        this.surcharge = amount;
    }

    public Section toSection() {
        return section;
    }

    public int getSurcharge() {
        return surcharge;
    }
}
