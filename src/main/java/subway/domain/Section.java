package subway.domain;

import java.util.Objects;

public class Section {

    private final Station left;
    private final Station right;
    private final int distance;

    public Section(Station left, Station right, int distance) {
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    public boolean hasStation(Station station) {
        return left.equals(station) || right.equals(station);
    }

    public Station getLeft() {
        return left;
    }

    public Station getRight() {
        return right;
    }

    public int getDistance() {
        return distance;
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
        return getDistance() == section.getDistance() && Objects.equals(getLeft(), section.getLeft())
                && Objects.equals(getRight(), section.getRight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLeft(), getRight(), getDistance());
    }
}
