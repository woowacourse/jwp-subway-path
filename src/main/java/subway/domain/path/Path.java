package subway.domain.path;

import java.util.List;
import subway.domain.core.Section;

public class Path {

    private final List<SectionEdge> sectionEdges;

    public Path(final List<SectionEdge> sectionEdges) {
        this.sectionEdges = sectionEdges;
    }

    public int calculateTotalDistance() {
        return sectionEdges.stream()
                .map(SectionEdge::toSection)
                .mapToInt(Section::getDistanceValue)
                .sum();
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }
}
