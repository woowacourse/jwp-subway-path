package subway.domain;

import subway.dao.entity.SectionEntity;

public class Section {
    private final Station startStation;
    private final Station endStation;
    private final int distance;

    public Section(Station startStation, Station endStation, int distance) {
        validateDistance(distance);
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("구간의 길이는 1 이상 이어야 합니다.");
        }
    }

    public static Section fromEntity(SectionEntity sectionEntity) {
        return new Section(new Station(sectionEntity.getStartStationName()), new Station(
                sectionEntity.getEndStationName()), sectionEntity.getDistance());
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
}
