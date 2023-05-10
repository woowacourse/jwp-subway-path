package subway.application.dto;

public class SectionCreateDto {

    private final Integer distance;
    private final String previousStationName;
    private final String nextStationName;

    public SectionCreateDto(final Integer distance, final String previousStationName, final String nextStationName) {
        this.distance = distance;
        this.previousStationName = previousStationName;
        this.nextStationName = nextStationName;
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
