package subway.domain;

import java.util.Objects;

public class Section {

    private final Station left;
    private final Station right;
    private final Distance distance;

    public Section(Station left, Station right, Distance distance) {
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    public boolean hasStation(Station station) {
        return left.equals(station) || right.equals(station);
    }

    public boolean hasSameStationName(Station leftStation, Station rightStation) {
        return leftStation.getName().equals(rightStation.getName());
    }

    public Station getLeft() {
        return left;
    }

    public Station getRight() {
        return right;
    }

    public int getDistance() {
        return distance.getValue();
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
        return Objects.equals(getLeft(), section.getLeft()) && Objects.equals(getRight(),
                section.getRight()) && Objects.equals(getDistance(), section.getDistance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLeft(), getRight(), getDistance());
    }
}
