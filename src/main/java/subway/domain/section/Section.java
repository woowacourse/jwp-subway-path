package subway.domain.section;

import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(Long id, Station upStation, Station downStation, Distance distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Long id, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public Section subtract(Section sectionToSubtract) {
        validateIsSameSection(sectionToSubtract);
        if (upStation.isSameStation(sectionToSubtract.getUpStation())) {
            Station newUpStation = sectionToSubtract.getDownStation();
            Distance newDistance = this.distance.subtract(sectionToSubtract.getDistance());
            return new Section(null, newUpStation, downStation, newDistance);
        }
        if (downStation.isSameStation(sectionToSubtract.getDownStation())) {
            Station newDownStation = sectionToSubtract.getUpStation();
            Distance newDistance = this.distance.subtract(sectionToSubtract.getDistance());
            return new Section(null, upStation, newDownStation, newDistance);
        }
        throw new IllegalArgumentException("현재 등록된 역 중에 하나를 포함해야합니다.");
    }

    public Section combine(Section sectionToCombine) {
        validateIsSameSection(sectionToCombine);
        if (downStation.isSameStation(sectionToCombine.getUpStation())) {
            Station newDownStation = sectionToCombine.getDownStation();
            Distance newDistance = this.distance.add(sectionToCombine.getDistance());
            return new Section(null, upStation, newDownStation, newDistance);
        }
        if (upStation.isSameStation(sectionToCombine.getDownStation())) {
            Station newUpStation = sectionToCombine.getUpStation();
            Distance newDistance = this.distance.add(sectionToCombine.getDistance());
            return new Section(null, newUpStation, downStation, newDistance);
        }
        throw new IllegalArgumentException("현재 등록된 역 중에 하나를 포함해야합니다.");
    }

    private void validateIsSameSection(Section otherSection) {
        if (isSameSection(otherSection)) {
            throw new IllegalArgumentException("이미 포함되어 있는 구간입니다.");
        }
    }

    private boolean isSameSection(Section otherSection) {
        return upStation.isSameStation(otherSection.getUpStation()) &&
                downStation.isSameStation(otherSection.getDownStation()) &&
                distance.equals(otherSection.getDistance());
    }

    public boolean isContainSection(Section targetSection) {
        return upStation.isSameStation(targetSection.getUpStation()) || downStation.isSameStation(targetSection.getDownStation());
    }

    public boolean isContainStation(Station targetStation) {
        return upStation.isSameStation(targetStation) || downStation.isSameStation(targetStation);
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public String getUpStationName() {
        return upStation.getName();
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    public String getDownStationName() {
        return downStation.getName();
    }

    public Distance getDistance() {
        return distance;
    }

    public int getDistanceValue() {
        return distance.getDistance();
    }

    public Line getLine() {
        if (upStation.getLineName().equals(downStation.getLineName())) {
            return upStation.getLine();
        }
        throw new IllegalArgumentException("노선을 찾을 수 없습니다.");
    }

    public Long getLineId() {
        return getLine().getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}
