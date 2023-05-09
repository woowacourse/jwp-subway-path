package subway.service.dto;

public class SectionCreateDto {

    private final Long lineId;
    private final Integer distance;
    private final String previousStationName;
    private final String nextStationName;

    public SectionCreateDto(final Long lineId, final Integer distance, final String previousStationName, final String nextStationName) {
        this.lineId = lineId;
        this.distance = distance;
        this.previousStationName = previousStationName;
        this.nextStationName = nextStationName;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getPreviousStationName() {
        return previousStationName;
    }

    public String getNextStationName() {
        return nextStationName;
    }

}
