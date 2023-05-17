package subway.dao.entity;

import subway.domain.station.Station;

public class SectionEntity {
    private final Long id;
    private final Long lineId;
    private final Station upBoundStation;
    private final Station downBoundStation;
    private final int distance;

    public SectionEntity(Long id, Long lineId, Station upBoundStation, Station downBoundStation, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upBoundStation = upBoundStation;
        this.downBoundStation = downBoundStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Station getUpBoundStation() {
        return upBoundStation;
    }

    public Station getDownBoundStation() {
        return downBoundStation;
    }

    public int getDistance() {
        return distance;
    }
}
