package subway.application.request;

public class CreateSectionRequest {

    private String upStationName;
    private String downStationName;
    private Long lineId;
    private Integer distance;

    public CreateSectionRequest() {
    }

    public CreateSectionRequest(final String upStationName, final String downStationName, final Long lineId, final Integer distance) {
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.lineId = lineId;
        this.distance = distance;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }
}
