package subway.route.domain.jgraph;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.line.domain.LineInfo;

public class WeightedEdgeWithLineInfo extends DefaultWeightedEdge {

    private final long lindId;
    private final LineInfo lineInfo;
    private final int distance;


    public WeightedEdgeWithLineInfo(long lindId, LineInfo lineInfo, int distance) {
        this.lindId = lindId;
        this.lineInfo = lineInfo;
        this.distance = distance;
    }

    public long getLindId() {
        return lindId;
    }

    public LineInfo getLineInfo() {
        return lineInfo;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "WeightedEdgeWithLineInfo{" +
                "lindId=" + lindId +
                ", lineInfo=" + lineInfo +
                ", distance=" + distance +
                '}';
    }
}
