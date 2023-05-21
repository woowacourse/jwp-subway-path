package subway.domain.Path;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.line.Line;
import subway.domain.section.Section;

public class SectionEdge extends DefaultWeightedEdge {

    private final Line line;

    public SectionEdge(Line line) {
        super();
        this.line = line;
    }

    public Line getLine() {
        return line;
    }
}
