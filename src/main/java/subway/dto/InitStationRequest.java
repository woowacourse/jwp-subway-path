package subway.dto;

public class InitStationRequest {

    private final String firstStationName;
    private final String lastStationName;
    private final Integer distance;
    private final Long lineId;

    public InitStationRequest(final String firstStationName, final String lastStationName, final Integer distance, final Long lineId) {
        this.firstStationName = firstStationName;
        this.lastStationName = lastStationName;
        this.distance = distance;
        this.lineId = lineId;
    }

    public String getFirstStationName() {
        return firstStationName;
    }

    public String getLastStationName() {
        return lastStationName;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
