package subway.route.application.dto.request;

import java.util.List;
import subway.route.domain.InterStationEdge;

public class PathRequestDto {

    private final long sourceId;
    private final long targetId;
    private final List<InterStationEdge> edges;

    public PathRequestDto(long sourceId, long targetId, List<InterStationEdge> edges) {
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
