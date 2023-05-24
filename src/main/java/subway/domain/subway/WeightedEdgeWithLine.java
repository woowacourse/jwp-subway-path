package subway.domain.subway;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.line.Line;
import subway.domain.line.Station;

public class WeightedEdgeWithLine extends DefaultWeightedEdge {
    private Line line;

    public WeightedEdgeWithLine(Line line) {
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
