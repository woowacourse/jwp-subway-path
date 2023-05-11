package subway.dto;

public class RegisterInnerStationRequest {

    private String newStationName;
    private String leftBaseStationName;
    private int leftDistance;
    private String rightBaseStationName;
    private int rightDistance;

    public RegisterInnerStationRequest() {
    }

    public RegisterInnerStationRequest(final String newStationName, String leftBaseStationName, int leftDistance, String rightBaseStationName, int rightDistance) {
        this.newStationName = newStationName;
        this.leftBaseStationName = leftBaseStationName;
        this.leftDistance = leftDistance;
        this.rightBaseStationName = rightBaseStationName;
        this.rightDistance = rightDistance;
    }

    public String getLeftBaseStationName() {
        return leftBaseStationName;
    }

    public int getLeftDistance() {
        return leftDistance;
    }

    public String getNewStationName() {
        return newStationName;
    }

    public String getRightBaseStationName() {
        return rightBaseStationName;
    }

    public int getRightDistance() {
        return rightDistance;
    }
}
