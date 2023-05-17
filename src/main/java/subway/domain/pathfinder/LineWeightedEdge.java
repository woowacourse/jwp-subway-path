package subway.domain.pathfinder;

import org.jgrapht.graph.DefaultWeightedEdge;

public class LineWeightedEdge extends DefaultWeightedEdge {
    private Long lineId;
    private Integer distance;

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
