package subway.jgraph;

import java.util.List;
import subway.domain.core.Section;
import subway.domain.path.Path;
import subway.domain.path.SectionEdge;

public class JgraphtPath implements Path {

    private final List<SectionEdge> sectionEdges;

    public JgraphtPath(final List<SectionEdge> sectionEdges) {
        this.sectionEdges = sectionEdges;
    }

    @Override
    public int calculateTotalDistance() {
        return sectionEdges.stream()
                .map(SectionEdge::toSection)
                .mapToInt(Section::getDistanceValue)
                .sum();
    }

    @Override
    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }
}
