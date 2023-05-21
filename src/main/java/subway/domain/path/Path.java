package subway.domain.path;

import java.util.List;

public interface Path {

    int calculateTotalDistance();

    List<SectionEdge> getSectionEdges();
}
