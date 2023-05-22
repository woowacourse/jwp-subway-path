package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

public class LineEdge extends DefaultWeightedEdge {
    private final Long lineId;

    public LineEdge(Long lineId) {
        this.lineId = lineId;
    }

    public Long getLineId() {
        return lineId;
    }
}
