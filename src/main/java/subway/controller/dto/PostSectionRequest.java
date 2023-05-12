package subway.controller.dto;

public class PostSectionRequest {

    private String upStationName;
    private String downStationName;
    private Long lineId;
    private Integer distance;

    public PostSectionRequest() {
    }

    public PostSectionRequest(String upStationName, String downStationName, Long lineId, Integer distance) {
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
