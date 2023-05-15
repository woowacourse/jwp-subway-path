package subway.dto;

public class StationInitResponse {

    private final Long upboundStationId;
    private final String upboundStationName;
    private final Long downboundStationId;
    private final String downboundStationName;

    public StationInitResponse(Long upboundStationId, String upboundStationName, Long downboundStationId, String downboundStationName) {
        this.upboundStationId = upboundStationId;
        this.upboundStationName = upboundStationName;
        this.downboundStationId = downboundStationId;
        this.downboundStationName = downboundStationName;
    }

    public Long getUpboundStationId() {
        return upboundStationId;
    }

    public String getUpboundStationName() {
        return upboundStationName;
    }

    public Long getDownboundStationId() {
        return downboundStationId;
    }

    public String getDownboundStationName() {
        return downboundStationName;
    }
}
