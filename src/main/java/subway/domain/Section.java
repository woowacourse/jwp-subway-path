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

    public boolean hasSameStationWith(final Section targetSection) {
        return hasStationOf(targetSection.left) || hasStationOf(targetSection.right);
    }

    private boolean hasStationOf(final Station station) {
        return left.equals(station) || right.equals(station);
    }

    public boolean isDistanceBiggerThan(final Section target) {
        return distance.isBiggerThan(target.distance);
    }

    public boolean includeSection(final Section target) {
        return target.right.equals(right) || target.left.equals(left);
    }

    public boolean containRightStationOf(final Section target) {
        return right.equals(target.right) || left.equals(target.right);
    }

    public boolean containLeftStationOf(final Section target) {
        return right.equals(target.left) || left.equals(target.left);
    }

    public Section makeRightSectionTo(final Section targetSection) {
        return new Section(left, targetSection.left, distance.sub(targetSection.distance));
    }

    public Section makeLeftSectionTo(final Section targetSection) {
        return new Section(targetSection.right, right, distance.sub(targetSection.distance));
    }

    public boolean hasLeftStation(final Station target) {
        return left.equals(target);
    }

    public boolean hasRightStation(final Station target) {
        return right.equals(target);
    }

    public Section merge(final Section target) {
        if (!right.equals(target.left)) {
            throw new IllegalStateException("두 섹션은 접합부가 달라 연결될 수 없습니다.");
        }

        return new Section(left, target.right, distance.add(target.distance));
    }

    public Station getLeft() {
        return left;
    }

    public Station getRight() {
        return right;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Section section = (Section) o;
        return left.equals(section.left) && right.equals(section.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
