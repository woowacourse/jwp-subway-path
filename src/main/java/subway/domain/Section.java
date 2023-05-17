package subway.domain;

import java.util.List;
import java.util.Objects;

import subway.domain.exception.EmptySectionException;

public class Section {

    private final Station upperStation;
    private final Station lowerStation;
    private final int distance;

    public Section(Station upperStation, Station lowerStation, int distance) {
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

    public boolean hasOverlapWith(Section other) {
        if (other.distance <= distance) {
            return hasUpperOverlapWith(other) || hasLowerOverlapWith(other);
        }
        return false;
    }

    public boolean contains(Station station) {
        return upperStation.equals(station) || lowerStation.equals(station);
    }

    public List<Section> splitIntoOneAnd(Section other) {
        validateHasOverlapWith(other);
        if (hasUpperOverlapWith(other)) {
            return List.of(
                    other,
                    new Section(other.lowerStation, lowerStation, distance - other.distance));
        }
        return List.of(
                new Section(upperStation, other.upperStation, distance - other.distance),
                other);
    }

    public Section mergeWith(Section other) {
        validateHasLinkWith(other);
        if (hasUpperLinkWith(other)) {
            return new Section(this.lowerStation, other.upperStation, this.distance + other.distance);
        }
        return new Section(this.upperStation, other.lowerStation, this.distance + other.distance);
    }

    private void validateHasLinkWith(Section other) {
        if (!this.hasLinkWith(other)) {
            throw new IllegalArgumentException("연결되는 구간이 아닙니다");
        }
    }

    private void validateHasOverlapWith(Section other) {
        if (!this.hasOverlapWith(other)) {
            throw new IllegalArgumentException("겹치는 구간이 아닙니다");
        }
    }

    private boolean hasLowerLinkWith(Section other) {
        return lowerStation.equals(other.upperStation);
    }

    private boolean hasUpperLinkWith(Section other) {
        return upperStation.equals(other.lowerStation);
    }

    private boolean hasUpperOverlapWith(Section other) {
        return upperStation.equals(other.upperStation);
    }

    private boolean hasLowerOverlapWith(Section other) {
        return lowerStation.equals(other.lowerStation);
    }

    public int getDistance() {
        return distance;
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

        if (distance != section.distance)
            return false;
        if (!Objects.equals(upperStation, section.upperStation))
            return false;
        return Objects.equals(lowerStation, section.lowerStation);
    }

    @Override
    public int hashCode() {
        int result = upperStation != null ? upperStation.hashCode() : 0;
        result = 31 * result + (lowerStation != null ? lowerStation.hashCode() : 0);
        result = 31 * result + distance;
        return result;
    }
}
