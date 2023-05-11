package subway.dao.entity;

import subway.domain.Station;

public class SectionEntity {
    private Long id;
    private Long lineId;
    private Station startStation;
    private Station endStation;
    private int distance;

    public SectionEntity() {
    }

    public SectionEntity(Long id, Long lineId, Station startStation, Station endStation, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
