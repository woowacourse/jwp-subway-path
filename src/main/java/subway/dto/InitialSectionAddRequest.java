package subway.dto;

public class InitialSectionAddRequest {
    private final Long lineId;
    private final Long firstStationId;
    private final Long secondStationId;
    private final Integer distance;

    public InitialSectionAddRequest(Long lineId, Long firstStationId, Long secondStationId, Integer distance) {
        this.lineId = lineId;
        this.firstStationId = firstStationId;
        this.secondStationId = secondStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getFirstStationId() {
        return firstStationId;
    }

    public Long getSecondStationId() {
        return secondStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
