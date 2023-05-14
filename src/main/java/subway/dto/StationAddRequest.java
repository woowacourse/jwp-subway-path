package subway.dto;

public class StationAddRequest {

    private final String lineName;
    private final String upLineStationName;
    private final String downLineStationName;
    private final int distance;

    public StationAddRequest(final String lineName, final String upLineStationName, final String downLineStationName, final int distance) {
        this.lineName = lineName;
        this.upLineStationName = upLineStationName;
        this.downLineStationName = downLineStationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getUpLineStationName() {
        return upLineStationName;
    }

    public String getDownLineStationName() {
        return downLineStationName;
    }

    public int getDistance() {
        return distance;
    }
}
