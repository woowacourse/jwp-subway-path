package subway.line.ui.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineAddStationRequest {

    private Long upStationId;
    private Long downStationId;
    @NotNull
    @Positive
    private Long newStationId;
    @Positive
    @NotNull
    private Long distance;

    private LineAddStationRequest() {
    }

    public LineAddStationRequest(Long upStationId, Long downStationId, Long newStationId,
            Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.newStationId = newStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getNewStationId() {
        return newStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
