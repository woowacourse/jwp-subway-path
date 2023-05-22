package subway.ui.dto.request;

public class SectionCreateRequest {

    private Long lineId;
    private String leftStationName;
    private String rightStationName;
    private Integer distance;

    private SectionCreateRequest() {
    }

    public SectionCreateRequest(
            final Long lineId,
            final String leftStationName,
            final String rightStationName,
            final Integer distance
    ) {
        this.lineId = lineId;
        this.leftStationName = leftStationName;
        this.rightStationName = rightStationName;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
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
