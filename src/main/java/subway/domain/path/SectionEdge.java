package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.core.Section;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;
    private final int amount;

    public SectionEdge(final Section section, int amount) {
        this.section = section;
        this.amount = amount;
    }

    public Section toSection() {
        return section;
    }

    public int getAmount() {
        return amount;
    }
}
