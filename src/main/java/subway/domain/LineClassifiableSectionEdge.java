package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.entity.Line;
import subway.domain.entity.Station;

public class LineClassifiableSectionEdge extends DefaultWeightedEdge {

    private Line line;

    public LineClassifiableSectionEdge(final Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }

    @Override
    protected Station getSource() {
        return (Station) super.getSource();
    }

    @Override
    protected Station getTarget() {
        return (Station) super.getTarget();
    }

}
