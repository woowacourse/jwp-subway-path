package subway.domain.subway;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.station.Station;

public class LineWeightedEdge extends DefaultWeightedEdge {

    private Long lineId;
    private int fareOfLine;

    public LineWeightedEdge() {
    }

    public LineWeightedEdge(final Long lineId, final int fareOfLine) {
        this.lineId = lineId;
        this.fareOfLine = fareOfLine;
    }

    public Station getSource() {
        return (Station) super.getSource();
    }

    public Station getTarget() {
        return (Station) super.getTarget();
    }

    public double getWeight() {
        return super.getWeight();
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(final Long lineId) {
        this.lineId = lineId;
    }

    public int getFareOfLine() {
        return fareOfLine;
    }

    public void setFareOfLine(final int fareOfLine) {
        this.fareOfLine = fareOfLine;
    }
}
