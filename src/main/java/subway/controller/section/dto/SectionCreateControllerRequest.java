package subway.controller.section.dto;

public class SectionCreateControllerRequest {
    private long upStationId;
    private long downStationId;
    private int distance;
    private long lineId;

    public SectionCreateControllerRequest(long upStationId, long downStationId, int distance, long lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    private SectionCreateControllerRequest() {

    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public long getLineId() {
        return lineId;
    }
}
