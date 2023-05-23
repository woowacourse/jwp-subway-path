package subway.service.dto;

public class SectionDto {

    private final Long lineId;
    private final String sourceStation;
    private final String targetStation;
    private final int distance;

    public SectionDto(
            final Long lineId,
            final String sourceStation,
            final String targetStation,
            final int distance
    ) {
        this.lineId = lineId;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getTargetStation() {
        return targetStation;
    }

    public int getDistance() {
        return distance;
    }
}
