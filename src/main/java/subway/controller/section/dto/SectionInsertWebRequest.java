package subway.controller.section.dto;

public class SectionInsertWebRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private Long lineId;

    public SectionInsertWebRequest(Long upStationId, Long downStationId, int distance, Long lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    private SectionInsertWebRequest() {

    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
