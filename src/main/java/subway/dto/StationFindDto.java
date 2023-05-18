package subway.dto;

public class StationFindDto {

    private final String lineName;
    private final String stationName;

    public StationFindDto(final String lineName, final String stationName) {
        this.lineName = lineName;
        this.stationName = stationName;
    }

    public String getLineName() {
        return lineName;
    }

    public String getStationName() {
        return stationName;
    }
}
