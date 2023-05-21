package subway.domain.section;

import java.util.Objects;
import subway.dao.entity.SectionEntity;
import subway.domain.station.Station;
import subway.exception.section.IllegalSectionException;

public class Section {
    private final Station upBoundStation;
    private final Station downBoundStation;
    private final Distance distance;

    public Section(Station upBoundStation, Station downBoundStation, Distance distance) {
        validateSameStation(upBoundStation, downBoundStation);
        this.upBoundStation = upBoundStation;
        this.downBoundStation = downBoundStation;
        this.distance = distance;
    }

    private void validateSameStation(Station upBoundStation, Station downBoundStation) {
        if (Objects.equals(upBoundStation, downBoundStation)) {
            throw new IllegalSectionException("추가하려는 상행역과 하행역의 이름이 같습니다.");
        }
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
