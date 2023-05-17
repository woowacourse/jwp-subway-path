package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.core.Section;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;
    private final int surcharge;
    private final long lineId;

    public SectionEdge(final Section section, final int surcharge, final long lineId) {
        this.section = section;
        this.surcharge = surcharge;
        this.lineId = lineId;
    }

    public Section toSection() {
        return section;
    }

    public int getSurcharge() {
        return surcharge;
    }

    public long getLineId() {
        return lineId;
    }

    @Override
    public String toString() {
        return "SectionEdge{" +
                "section=" + section +
                ", surcharge=" + surcharge +
                ", lineId=" + lineId +
                '}';
    }
}
