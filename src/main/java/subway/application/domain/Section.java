package subway.application.domain;

import java.util.Objects;

public class Section {

    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public boolean hasAnySameStationWith(Section targetSection) {
        return hasStationOf(targetSection.upStation) || hasStationOf(targetSection.downStation);
    }

    private boolean hasStationOf(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public boolean isDistanceBiggerOrEqualThan(Section target) {
        return distance.isBiggerThan(target.distance) || distance.equals(target.distance);
    }

    public boolean overlaps(Section target) {
        return target.downStation.equals(downStation) || target.upStation.equals(upStation);
    }

    public boolean containsDownStationOf(Section target) {
        return downStation.equals(target.downStation) || upStation.equals(target.downStation);
    }

    public boolean containsUpStationOf(Section target) {
        return downStation.equals(target.upStation) || upStation.equals(target.upStation);
    }

    public Section makeConnectionTo(Section target) {
        if (containsUpStationOf(target)) {
            return new Section(target.downStation, downStation, distance.sub(target.distance));
        }
        return new Section(upStation, target.upStation, distance.sub(target.distance));
    }

    public boolean hasUpStation(Station target) {
        return upStation.equals(target);
    }

    public boolean hasDownStation(Station target) {
        return downStation.equals(target);
    }

    public Section merge(Section target) {
        if (!downStation.equals(target.upStation)) {
            throw new IllegalStateException("두 섹션은 접합부가 달라 연결될 수 없습니다.");
        }

        return new Section(upStation, target.downStation, distance.add(target.distance));
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return upStation.equals(section.upStation) && downStation.equals(section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation);
    }
}
