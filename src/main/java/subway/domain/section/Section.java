package subway.domain.section;

import subway.domain.Distance;
import subway.domain.Station;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static subway.domain.Position.MID;

public class Section {

    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    private Section(final Station upStation, final Station downStation, final Distance distance) {
        validateDuplicationStation(upStation, downStation);

        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(final Station upStation, final Station downStation, final Distance distance) {
        return new Section(upStation, downStation, distance);
    }

    private void validateDuplicationStation(final Station upStation, final Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException("구간을 등록하는 두 역이 같을 수 없습니다.");
        }
    }

    public boolean isEndSection() {
        final Station upStation = getUpStation();
        final Station downStation = getDownStation();

        return upStation.isUpStation() || downStation.isDownStation();
    }

    public Section createUpSection(final Section originSection) {
        final Station upStation = originSection.getUpStation();
        final Distance distance = calculateDistance(originSection, Section::getUpStation);
        final Station newStation = findOtherStationWithout(originSection);
        newStation.changeDirection(MID);
        return Section.of(upStation, newStation, distance);
    }

    public Section createDownSection(final Section originSection) {
        final Station downStation = originSection.getDownStation();
        final Distance distance = calculateDistance(originSection, Section::getDownStation);
        final Station newStation = findOtherStationWithout(originSection);
        newStation.changeDirection(MID);
        return Section.of(newStation, downStation, distance);
    }

    private Distance calculateDistance(final Section originSection,
                                       final Function<Section, Station> getStationFromSection) {
        final Distance originDistance = originSection.getDistance();
        final Station currentStation = getStationFromSection.apply(this);
        final Station originStation = getStationFromSection.apply(originSection);

        if (currentStation.equals(originStation)) {
            return this.distance;
        }

        if (this.distance.getDistance() >= originDistance.getDistance()) {
            throw new IllegalArgumentException("등록하려는 구간의 길이는 원본 구간의 길이 미만이어야 합니다.");
        }

        return originDistance.minus(this.distance);
    }

    private Station findOtherStationWithout(final Section removeSection) {
        final Set<Station> ownStations = new HashSet<>(Set.of(this.getUpStation(), this.getDownStation()));
        final Set<Station> removeStations = new HashSet<>(Set.of(removeSection.getUpStation(), removeSection.getDownStation()));
        ownStations.removeAll(removeStations);

        if (ownStations.size() != 1) {
            throw new IllegalArgumentException("현재 구역의 역이 삭제되지 않았습니다. 삭제하려는 구역의 역이 현재 구역의 역에 존재하는지 확인해주세요");
        }

        return ownStations.iterator().next();
    }

    public boolean contains(final Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public Station findOtherStation(final Station station) {
        if (!contains(station)) {
            throw new IllegalArgumentException("해당 구간에 입력한 역이 존재하지 않습니다.");
        }

        if (upStation.equals(station)) {
            return downStation;
        }
        return upStation;
    }

    public boolean isSameSection(final Section findSection) {
        final Station upFindStation = findSection.getUpStation();
        final Station downFindStation = findSection.getDownStation();

        return (upStation.equals(upFindStation) && downStation.equals(downFindStation))
                || (upStation.equals(downFindStation) && downStation.equals(upFindStation));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Section section = (Section) o;
        return Objects.equals(upStation, section.upStation)
                && Objects.equals(downStation, section.downStation)
                && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
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
}
