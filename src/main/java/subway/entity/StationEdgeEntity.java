package subway.entity;

import subway.domain.StationEdge;

public class StationEdgeEntity {

    private final Long id;
    private final Long lineId;
    private final Long downStationId;

    private final Integer distance;
    private final Long previousStationEdgeId;

    public StationEdgeEntity(Long lineId, Long downStationId, Integer distance, Long previousStationEdgeId) {
        this.id = null;
        this.lineId = lineId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.previousStationEdgeId = previousStationEdgeId;
    }

    public StationEdgeEntity(Long id, Long lineId, Long downStationId, Integer distance, Long previousStationEdgeId) {
        this.id = id;
        this.lineId = lineId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.previousStationEdgeId = previousStationEdgeId;
    }

    public static StationEdgeEntity of(Long lineId, StationEdge stationEdge, Long previousStationEdgeId) {
        return new StationEdgeEntity(lineId, stationEdge.getDownStationId(), stationEdge.getDistance(),
                previousStationEdgeId);
    }

    public static StationEdgeEntity of(Long id, Long lineId, StationEdge stationEdge, Long previousStationEdgeId) {
        return new StationEdgeEntity(id, lineId, stationEdge.getDownStationId(), stationEdge.getDistance(),
                previousStationEdgeId);
    }

    public StationEdge toDomain() {
        return new StationEdge(downStationId, distance);
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getPreviousStationEdgeId() {
        return previousStationEdgeId;
    }
}
