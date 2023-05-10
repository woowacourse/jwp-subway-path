package subway.domain;

import java.util.Objects;

public class Section {

    private final Station left;
    private final Station right;
    private final Distance distance;

    public Section(final Station left, final Station right, final Distance distance) {
        this.left = left;
        this.right = right;
        this.distance = distance;
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

    public Long getLeftId() {
        return left.getId();
    }

    public Long getRightId() {
        return right.getId();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(left, section.left) && Objects.equals(right, section.right)
                && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "left=" + left +
                ", right=" + right +
                ", distance=" + distance +
                '}';
    }
}
