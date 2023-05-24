package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

public final class LineRecordedDefaultWeightedEdge extends DefaultWeightedEdge {

    private final Long lineId;

    public LineRecordedDefaultWeightedEdge(Long lineId) {
        this.lineId = lineId;
    }

    public Long getLineId() {
        return lineId;
    }
}
