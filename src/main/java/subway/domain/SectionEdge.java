package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;
    private final Long lineId;

    public SectionEdge(Section section, Long lineId) {
        this.section = section;
        this.lineId = lineId;
    }

    public Section getSection() {
        return section;
    }

    public Long getLineId() {
        return lineId;
    }
}
