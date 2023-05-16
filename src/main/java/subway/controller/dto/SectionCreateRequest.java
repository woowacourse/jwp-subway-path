package subway.controller.dto;

public class SectionCreateRequest {

    private String leftStationName;
    private String rightStationName;
    private Integer distance;

    private SectionCreateRequest() {
    }

    public SectionCreateRequest(String leftStationName, String rightStationName, Integer distance) {
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

    public Integer getDistance() {
        return distance;
    }
}
