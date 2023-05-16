package subway.dto;

public class SectionDto {

    private final Long lineId;
    private final Long upperStation;
    private final Long lowerStation;
    private final Integer distance;

    public SectionDto(final Long lineId, final Long upperStation, final Long lowerStation, final Integer distance) {
        this.lineId = lineId;
        this.upperStation = upperStation;
        this.lowerStation = lowerStation;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpperStation() {
        return upperStation;
    }

    public Long getLowerStation() {
        return lowerStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
