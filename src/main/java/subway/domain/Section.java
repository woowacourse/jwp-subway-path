package subway.domain;

import subway.dao.entity.SectionEntity;

public class Section {
    private final Station upBoundStation;
    private final Station downBoundStation;
    private final Distance distance;

    public Section(Station upBoundStation, Station downBoundStation, Distance distance) {
        this.upBoundStation = upBoundStation;
        this.downBoundStation = downBoundStation;
        this.distance = distance;
    }

    public static Section fromEntity(SectionEntity sectionEntity) {
        return new Section(sectionEntity.getUpBoundStation(), sectionEntity.getDownBoundStation(), new Distance(sectionEntity.getDistance()));
    }

    public Station getUpBoundStation() {
        return upBoundStation;
    }

    public String getUpBoundStationName() {
        return upBoundStation.getName();
    }

    public Station getDownBoundStation() {
        return downBoundStation;
    }

    public String getDownBoundStationName() {
        return downBoundStation.getName();
    }

    public int getDistance() {
        return distance.getDistance();
    }
}
