package subway.dto;

public class InitStationsRequest {

    private Long leftStationId;
    private Long rightStationId;
    private int distance;

    public InitStationsRequest() {
    }

    public InitStationsRequest(final Long leftStationId, final Long rightStationId, final int distance) {
        this.leftStationId = leftStationId;
        this.rightStationId = rightStationId;
        this.distance = distance;
    }

    public Long getLeftStationId() {
        return leftStationId;
    }

    public Long getRightStationId() {
        return rightStationId;
    }

    public int getDistance() {
        return distance;
    }
}
