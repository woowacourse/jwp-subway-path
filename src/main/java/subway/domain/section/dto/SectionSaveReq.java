package subway.domain.section.dto;

public class SectionSaveReq {

    private final Long lineId;
    private final Long sourceStationId;
    private final Long targetStationId;
    private final int distance;

    public SectionSaveReq(final Long lineId, final Long sourceStationId, final Long targetStationId,
                          final int distance) {
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

    public int getDistance() {
        return distance;
    }
}
