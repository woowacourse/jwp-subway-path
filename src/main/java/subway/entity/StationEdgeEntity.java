package subway.entity;

import subway.domain.StationEdge;

public class StationEdgeEntity implements Entity{

    private final Long id;
    private final Long lineId;
    private final Long downStationId;

    private final Integer distance;
    private final Long previousStationEdgeId;

    public StationEdgeEntity(
            final Long lineId,
            final Long downStationId,
            final Integer distance,
            final Long previousStationEdgeId
    ) {
        this.id = null;
        this.lineId = lineId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.previousStationEdgeId = previousStationEdgeId;
    }

    public StationEdgeEntity(
            final Long id,
            final Long lineId,
            final Long downStationId,
            final Integer distance,
            final Long previousStationEdgeId
    ) {
        this.id = id;
        this.lineId = lineId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.previousStationEdgeId = previousStationEdgeId;
    }

    public static StationEdgeEntity of(
            final Long lineId,
            final StationEdge stationEdge,
            final Long previousStationEdgeId
    ) {
        return new StationEdgeEntity(lineId, stationEdge.getDownStationId(), stationEdge.getDistance(),
                previousStationEdgeId);
    }

    public static StationEdgeEntity of(
            final Long id,
            final Long lineId,
            final StationEdge stationEdge,
            final Long previousStationEdgeId
    ) {
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
