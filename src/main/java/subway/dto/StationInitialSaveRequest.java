package subway.dto;

public class StationInitialSaveRequest {

    private final String lineName;
    private final String leftStationName;
    private final String rightStationName;
    private final Integer distance;

    public StationInitialSaveRequest(
            final String lineName,
            final String leftStationName,
            final String rightStationName,
            final Integer distance
    ) {
        this.lineName = lineName;
        this.leftStationName = leftStationName;
        this.rightStationName = rightStationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLeftStationName() {
        return leftStationName;
    }

    public String getRightStationName() {
        return rightStationName;
    }

    public Integer getDistance() {
        return distance;
    }
}
