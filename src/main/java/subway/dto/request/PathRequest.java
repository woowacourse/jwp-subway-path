package subway.dto.request;

import javax.validation.constraints.Positive;

public class PathRequest {
    @Positive(message = "출발역의 ID는 1 이상의 숫자여야 합니다.")
    private Long sourceStationId;

    @Positive(message = "도착역의 ID는 1 이상의 숫자여야 합니다.")

    private Long destinationStationId;

    public PathRequest(final Long sourceStationId, final Long destinationStationId) {
        this.sourceStationId = sourceStationId;
        this.destinationStationId = destinationStationId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getDestinationStationId() {
        return destinationStationId;
    }
}
