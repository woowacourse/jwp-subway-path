package subway.adapter.out.graph;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.line.Line;
import subway.domain.section.Section;

public class LineWeightedEdge extends DefaultWeightedEdge {

    private Line line;
    private Section section;

    private LineWeightedEdge() {
    }

    public LineWeightedEdge(final Line line, final Section section) {
        this.line = line;
        this.section = section;
    }

    public Line getLine() {
        return line;
    }

    public Section getSection() {
        return section;
    }
}
