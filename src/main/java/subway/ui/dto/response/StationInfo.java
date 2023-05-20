package subway.ui.dto.response;

public class StationInfo {

    private String stationName;

    private StationInfo() {
    }

    public StationInfo(final String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
