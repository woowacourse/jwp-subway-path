package subway.domain;

public class PairId {
    private Long preStationId;
    private Long StationId;

    public PairId(Long preStationId, Long stationId) {
        this.preStationId = preStationId;
        StationId = stationId;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return StationId;
    }
}
