package subway.route.dto.request;

import java.util.List;
import subway.route.domain.InterStationEdge;

public class PathRequest {

    private final long sourceId;
    private final long targetId;
    private final List<InterStationEdge> edges;

    public PathRequest(long sourceId, long targetId, List<InterStationEdge> edges) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.edges = edges;
    }

    public long getSourceId() {
        return sourceId;
    }

    public long getTargetId() {
        return targetId;
    }

    public List<InterStationEdge> getGraph() {
        return edges;
    }
}
