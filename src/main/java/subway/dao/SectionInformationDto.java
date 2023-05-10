package subway.dao;

public class SectionInformationDto {
    private final String startStationName;
    private final String endStationName;
    private final int distance;

    public SectionInformationDto(final String startStationName, final String endStationName, final int distance) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.distance = distance;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public int getDistance() {
        return distance;
    }
}
