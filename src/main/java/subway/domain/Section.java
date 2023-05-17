package subway.domain;

import java.util.List;
import java.util.Objects;

import subway.domain.exception.EmptySectionException;
import subway.domain.exception.IllegalSectionException;

public class Section {

    private final Station upperStation;
    private final Station lowerStation;
    private final Distance distance;

    public Section(Station upperStation, Station lowerStation, int distance) {
        this(upperStation, lowerStation, new Distance(distance));
    }

    public Section(Station upperStation, Station lowerStation, Distance distance) {
        validateNonNull(upperStation, lowerStation);
        this.upperStation = upperStation;
        this.lowerStation = lowerStation;
        this.distance = distance;
    }

    private void validateNonNull(Station upperStation, Station lowerStation) {
        if (Objects.isNull(upperStation) || Objects.isNull(lowerStation)) {
            throw new EmptySectionException();
        }
    }

    public boolean hasLinkWith(Section other) {
        return hasUpperLinkWith(other) || hasLowerLinkWith(other);
    }

    public boolean hasOverlapWith(Section part) {
        return hasUpperOverlapWith(part) || hasLowerOverlapWith(part);
    }

    public boolean contains(Station station) {
        return upperStation.equals(station) || lowerStation.equals(station);
    }

    public List<Section> splitIntoOneAnd(Section other) {
        validateHasOverlapWith(other);
        if (hasUpperOverlapWith(other)) {
            return List.of(
                    other,
                    new Section(other.lowerStation, lowerStation, distance.gapBetween(other.distance)));
        }
        return List.of(
                new Section(upperStation, other.upperStation, distance.gapBetween(other.distance)),
                other);
    }

    public Section mergeWith(Section other) {
        validateHasLinkWith(other);
        if (hasUpperLinkWith(other)) {
            return new Section(this.lowerStation, other.upperStation, this.distance.sum(other.distance));
        }
        return new Section(this.upperStation, other.lowerStation, this.distance.sum(other.distance));
    }

    private void validateHasLinkWith(Section other) {
        if (!this.hasLinkWith(other)) {
            throw new IllegalSectionException("연결되는 구간이 아닙니다");
        }
    }

    private void validateHasOverlapWith(Section other) {
        if (!this.hasOverlapWith(other)) {
            throw new IllegalSectionException("겹치는 구간이 아닙니다");
        }
    }

    private boolean hasLowerLinkWith(Section other) {
        return lowerStation.equals(other.upperStation);
    }

    private boolean hasUpperLinkWith(Section other) {
        return upperStation.equals(other.lowerStation);
    }

    private boolean hasUpperOverlapWith(Section part) {
        if (this.distance.isLongerThan(part.distance)) {
            return upperStation.equals(part.upperStation);
        }
        return false;
    }

    private boolean hasLowerOverlapWith(Section part) {
        if (this.distance.isLongerThan(part.distance)) {
            return lowerStation.equals(part.lowerStation);
        }
        return false;
    }

    public int getDistance() {
        return distance.getValue();
    }

    public List<Station> getStations() {
        return List.of(upperStation, lowerStation);
    }

    public Station getUpperStation() {
        return upperStation;
    }

    public Station getLowerStation() {
        return lowerStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Section section = (Section)o;

        if (!upperStation.equals(section.upperStation))
            return false;
        if (!lowerStation.equals(section.lowerStation))
            return false;
        return distance.equals(section.distance);
    }

    @Override
    public int hashCode() {
        int result = upperStation.hashCode();
        result = 31 * result + lowerStation.hashCode();
        result = 31 * result + distance.hashCode();
        return result;
    }
}
