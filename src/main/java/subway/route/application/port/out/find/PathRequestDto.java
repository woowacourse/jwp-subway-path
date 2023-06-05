package subway.route.application.port.out.find;

import subway.route.domain.Edges;

public class PathRequestDto {

    private final long sourceId;
    private final long targetId;
    private final Edges edges;

    public PathRequestDto(final long sourceId, final long targetId, final Edges edges) {
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
