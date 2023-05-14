package subway.domain;

import subway.dao.entity.SectionEntity;
import subway.exception.IllegalDistanceException;

public class Section {
    private final Station upBoundStation;
    private final Station downBoundStation;
    private final int distance;

    public Section(Station upBoundStation, Station downBoundStation, int distance) {
        validateDistance(distance);
        this.upBoundStation = upBoundStation;
        this.downBoundStation = downBoundStation;
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalDistanceException("구간의 길이는 1 이상 이어야 합니다.");
        }
    }

    public static Section fromEntity(SectionEntity sectionEntity) {
        return new Section(sectionEntity.getUpBoundStation(), sectionEntity.getDownBoundStation(), sectionEntity.getDistance());
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
        return distance;
    }
}
