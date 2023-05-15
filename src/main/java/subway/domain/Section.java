package subway.domain;

import java.util.List;
import java.util.Objects;

public class Section {

    private final Station upperStation;
    private final Station lowerStation;
    private final int distance;

    public Section(Station upperStation, Station lowerStation, int distance) {
        this.upperStation = upperStation;
        this.lowerStation = lowerStation;
        this.distance = distance;
    }

    public boolean canConnect(Section other) {
        return upperStation.equals(other.lowerStation) || lowerStation.equals(other.upperStation);
    }

    public boolean covers(Section other) {
        if (other.distance <= distance) {
            return upperStation.equals(other.upperStation) || lowerStation.equals(other.lowerStation);
        }
        return false;
    }

    public boolean contains(Station station) {
        return upperStation.equals(station) || lowerStation.equals(station);
    }

    public Section getPartOtherThan(Section part) {
        validateThisCovers(part);
        if (part.contains(upperStation)) {
            return new Section(part.lowerStation, lowerStation, distance - part.distance);
        }
        return new Section(upperStation, part.upperStation, distance - part.distance);
    }

    public Section mergeWith(Section other) {
        validateConnectedWith(other);
        if (this.contains(other.upperStation)) {
            return new Section(this.upperStation, other.lowerStation, this.distance + other.distance);
        }
        return new Section(this.lowerStation, other.upperStation, this.distance + other.distance);
    }

    private void validateConnectedWith(Section section) {
        if (!this.canConnect(section)) {
            throw new IllegalArgumentException("연결되는 구간이 아닙니다");
        }
    }

    private void validateThisCovers(Section other) {
        if (!this.covers(other)) {
            throw new IllegalArgumentException("겹치는 구간이 아닙니다");
        }
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
