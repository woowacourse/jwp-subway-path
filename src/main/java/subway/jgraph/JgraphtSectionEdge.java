package subway.jgraph;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.core.Section;
import subway.domain.path.SectionEdge;

public class JgraphtSectionEdge extends DefaultWeightedEdge implements SectionEdge {

    private final Section section;
    private final int surcharge;
    private final long lineId;

    public JgraphtSectionEdge(final Section section, final int surcharge, final long lineId) {
        this.section = section;
        this.surcharge = surcharge;
        this.lineId = lineId;
    }

    @Override
    public Section toSection() {
        return section;
    }

    @Override
    public int getSurcharge() {
        return surcharge;
    }

    @Override
    public long getLineId() {
        return lineId;
    }
}
