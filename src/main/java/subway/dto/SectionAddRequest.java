package subway.dto;

public class SectionAddRequest {
    private final Long lineId;
    private final Long stationId;
    private final Long sourceId;
    private final Long targetId;
    private final Integer distance;

    public SectionAddRequest(Long lineId, Long stationId, Long sourceId, Long targetId, Integer distance) {
        this.lineId = lineId;
        this.stationId = stationId;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public Integer getDistance() {
        return distance;
    }
}
