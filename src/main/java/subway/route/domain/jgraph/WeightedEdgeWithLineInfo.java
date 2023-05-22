package subway.route.domain.jgraph;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.line.domain.LineMetadata;

public class WeightedEdgeWithLineInfo extends DefaultWeightedEdge {

    private final long lindId;
    private final LineMetadata lineMetadata;
    private final int distance;


    public WeightedEdgeWithLineInfo(long lindId, LineMetadata lineMetadata, int distance) {
        this.lindId = lindId;
        this.lineMetadata = lineMetadata;
        this.distance = distance;
    }

    public long getLindId() {
        return lindId;
    }

    public LineMetadata getLineInfo() {
        return lineMetadata;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "WeightedEdgeWithLineInfo{" +
                "lindId=" + lindId +
                ", lineMetadata=" + lineMetadata +
                ", distance=" + distance +
                '}';
    }
}
