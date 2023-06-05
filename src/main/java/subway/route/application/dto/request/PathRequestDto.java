package subway.route.application.dto.request;

import subway.route.domain.Edges;

public class PathRequestDto {

    private final long sourceId;
    private final long targetId;
    private final Edges edges;

    public PathRequestDto(long sourceId, long targetId, Edges edges) {
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

    public Edges getGraph() {
        return edges;
    }
}
