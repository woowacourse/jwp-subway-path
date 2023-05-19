package subway.application.route.port.out.find;

import subway.domain.route.Graph;

public class PathRequestDto {

    private final long sourceId;
    private final long targetId;
    private final Graph graph;

    public PathRequestDto(final long sourceId, final long targetId, final Graph graph) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.graph = graph;
    }

    public long getSourceId() {
        return sourceId;
    }

    public long getTargetId() {
        return targetId;
    }

    public Graph getGraph() {
        return graph;
    }
}
