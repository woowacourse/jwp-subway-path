package subway.dto;

public class ShortPathRequest {

    private String startStationName;
    private String endStationName;

    public ShortPathRequest() {
    }

    public ShortPathRequest(String startStationName, String endStationName) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

}
