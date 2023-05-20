package subway.adapter.out.graph;

import org.jgrapht.graph.DefaultWeightedEdge;

public class LineWeightedEdge extends DefaultWeightedEdge {
    private Long lineId;

    public LineWeightedEdge() {
    }

    public LineWeightedEdge(final Long lineId) {
        this.lineId = lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Long getLineId() {
        return lineId;
    }
}
