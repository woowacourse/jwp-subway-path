package subway.dto;

public class RegisterStationRequest {

    private Long newStationId;
    private Long baseStationId;
    private String direction;
    private int distance;

    public RegisterStationRequest() {
    }

    public RegisterStationRequest(Long newStationId, Long baseStationId, String direction, int distance) {
        this.newStationId = newStationId;
        this.baseStationId = baseStationId;
        this.direction = direction;
        this.distance = distance;
    }

    public Long getNewStationId() {
        return newStationId;
    }

    public Long getBaseStationId() {
        return baseStationId;
    }

    public String getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }
}
