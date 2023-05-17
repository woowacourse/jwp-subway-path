package subway.dto;

public class SectionDto {

    private final Long lineId;
    private final String sourceStation;
    private final String targetStation;
    private final Integer distance;

    public SectionDto() {
        this(null, null, null, null);
    }

    public SectionDto(final Long lineId, final String sourceStation, final String targetStation, final Integer distance) {
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

    public Integer getDistance() {
        return distance;
    }
}
