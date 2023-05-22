package subway.domain;

import java.util.Objects;

public class Section {

    private final Station left;
    private final Station right;
    private final Distance distance;

    public Section(
            final Station left,
            final Station right,
            final Distance distance
    ) {
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    public Section(
            final Station left,
            final Station right,
            final int distance
    ) {
        this.left = left;
        this.right = right;
        this.distance = new Distance(distance);
    }

    public void validateSameStation(final Station leftStation, final Station rightStation) {
        if (leftStation.getName().equals(rightStation.getName())) {
            throw new IllegalArgumentException("구간의 역 이름은 같을 수 없습니다.");
        }
    }

    public boolean hasStation(final Station station) {
        return left.equals(station) || right.equals(station);
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
