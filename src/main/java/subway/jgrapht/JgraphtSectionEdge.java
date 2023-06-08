package subway.jgrapht;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.path.SectionEdge;
import subway.domain.section.Section;

public class JgraphtSectionEdge extends DefaultWeightedEdge implements SectionEdge {

    private final Section section;
    private final Long lineId;

    public JgraphtSectionEdge(Section section, Long lineId) {
        this.section = section;
        this.lineId = lineId;
    }

    @Override
    public Section getSection() {
        return section;
    }

    @Override
    public Long getLineId() {
        return lineId;
    }
}
