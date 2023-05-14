package subway.dao.entity;

import subway.domain.Station;

public class SectionEntity {
    private Long id;
    private Long lineId;
    private Station upBoundStation;
    private Station downBoundStation;
    private int distance;

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
