package subway.dto.service;

import subway.domain.StationEdge;

public class WeightedLineEdge {
    private final StationEdge stationEdge;
    private final Long lineId;

    public WeightedLineEdge(StationEdge stationEdge, Long lineId) {
        this.stationEdge = stationEdge;
        this.lineId = lineId;
    }

    public StationEdge getStationEdge() {
        return stationEdge;
    }
    public int getWeight() {
        return stationEdge.getDistance().getValue();
    }

    public Long getLineId() {
        return lineId;
    }
}