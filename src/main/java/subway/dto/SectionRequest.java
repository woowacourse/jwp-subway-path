package subway.dto;

public class SectionRequest {

    private final Long lineId;
    private final Long sourceStationId;
    private final Long targetStationId;
    private final Integer distance;

    public SectionRequest() {
        this(null, null, null, null);
    }

    public SectionRequest(final Long lineId, final Long sourceStationId,
                          final Long targetStationId, final Integer distance) {
        this.lineId = lineId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
