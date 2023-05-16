package subway.route.domain.jgraph;

import org.jgrapht.graph.DefaultWeightedEdge;

public class WeightedEdgeWithLineInfo extends DefaultWeightedEdge {

    private final long lindId;
    private final String lineName;
    private final int distance;


    public WeightedEdgeWithLineInfo(long lindId, String lineName, int distance) {
        this.lindId = lindId;
        this.lineName = lineName;
        this.distance = distance;
    }

    public long getLindId() {
        return lindId;
    }

    public String getLineName() {
        return lineName;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "WeightedEdgeWithLineInfo{" +
                "lindId=" + lindId +
                ", lineName='" + lineName + '\'' +
                ", distance=" + distance +
                '}';
    }
}
