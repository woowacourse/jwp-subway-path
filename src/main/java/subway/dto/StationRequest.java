package subway.dto;

public class StationRequest {

    private String registerStationName;
    private String lineName;
    private String baseStation;
    private String direction;
    private int distance;

    public StationRequest() {
    }

    public StationRequest(
        final String registerStationName,
        final String lineName,
        final String baseStation,
        final String direction,
        final int distance
    ) {
        this.registerStationName = registerStationName;
        this.lineName = lineName;
        this.baseStation = baseStation;
        this.direction = direction;
        this.distance = distance;
    }

    public String getRegisterStationName() {
        return registerStationName;
    }

    public String getLineName() {
        return lineName;
    }

    public String getBaseStation() {
        return baseStation;
    }

    public String getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }
}
