package subway.dto.request;

import javax.validation.constraints.NotNull;

public class PathRequest {

    @NotNull
    private final Long sourceId;
    @NotNull
    private final Long destinationId;

    public PathRequest(Long sourceId, Long destinationId) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getDestinationId() {
        return destinationId;
    }
}
