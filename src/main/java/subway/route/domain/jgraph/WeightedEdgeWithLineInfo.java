package subway.route.domain.jgraph;

import org.jgrapht.graph.DefaultWeightedEdge;

public class WeightedEdgeWithLineInfo extends DefaultWeightedEdge {

    private final long lindId;
    private final String lineName;


    public WeightedEdgeWithLineInfo(long lindId, String lineName) {
        this.lindId = lindId;
        this.lineName = lineName;
    }

    public long getLindId() {
        return lindId;
    }

    public String getLineName() {
        return lineName;
    }
}
