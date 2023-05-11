package subway.dto;

public class StationAddRequest {

    private final Long previousStationId;
    private final Long nextStationId;
    private final Integer distance;

    private StationAddRequest() {
        this.distance = null;
        this.previousStationId = null;
        this.nextStationId = null;
    };

    public StationAddRequest(Long previousStationId, Long nextStationId, Integer distance) {
        this.previousStationId = previousStationId;
        this.nextStationId = nextStationId;
        this.distance = distance;
    }

    public Long getPreviousStationId() {
        return previousStationId;
    }

    public Long getNextStationId() {
        return nextStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
