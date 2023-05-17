package subway.domain.pathfinder;

import org.jgrapht.graph.DefaultWeightedEdge;

public class LineWeightedEdge extends DefaultWeightedEdge {
    private final Long lineId;
    private final Integer distance;

    public LineWeightedEdge(Long lineId, Integer distance) {
        this.lineId = lineId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }
}
