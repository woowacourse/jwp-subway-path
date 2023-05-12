package subway.dto;

public class RegisterStationsRequest {

    private String leftStationName;
    private String rightStationName;
    private int distance;

    public RegisterStationsRequest() {
    }

    public RegisterStationsRequest(final String leftStationName, final String rightStationName, final int distance) {
        this.leftStationName = leftStationName;
        this.rightStationName = rightStationName;
        this.distance = distance;
    }

    public String getLeftStationName() {
        return leftStationName;
    }

    public String getRightStationName() {
        return rightStationName;
    }

    public int getDistance() {
        return distance;
    }
}
