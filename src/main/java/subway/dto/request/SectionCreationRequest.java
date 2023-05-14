package subway.dto.request;

import javax.validation.constraints.Positive;

public class SectionCreationRequest {

    private Long lineId;
    private Long upwardStationId;
    private Long downwardStationId;
    @Positive
    private Integer distance;

    public SectionCreationRequest() {
    }

    public SectionCreationRequest(final Long lineId, final Long upwardStationId, final Long downwardStationId, final Integer distance) {
        this.lineId = lineId;
        this.upwardStationId = upwardStationId;
        this.downwardStationId = downwardStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpwardStationId() {
        return upwardStationId;
    }

    public Long getDownwardStationId() {
        return downwardStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
