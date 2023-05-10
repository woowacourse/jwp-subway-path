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

    public boolean hasSameStationWith(Section targetSection) {
        return hasStationOf(targetSection.left) || hasStationOf(targetSection.right);
    }

    private boolean hasStationOf(Station station) {
        return left.equals(station) || right.equals(station);
    }

    public boolean isDistanceBiggerThan(Section target) {
        return distance.isBiggerThan(target.distance);
    }

    public boolean includeSection(Section target) {
        return target.right.equals(right) || target.left.equals(left);
    }

    public boolean containRightStationOf(Section target) {
        return right.equals(target.right) || left.equals(target.right);
    }

    public boolean containLeftStationOf(Section target) {
        return right.equals(target.left) || left.equals(target.left);
    }

    public Section makeRightSectionTo(Section targetSection) {
        return new Section(left, targetSection.left, distance.sub(targetSection.distance));
    }

    public Section makeLeftSectionTo(Section targetSection) {
        return new Section(targetSection.right, right, distance.sub(targetSection.distance));
    }

    public boolean hasLeftStation(Station target) {
        return left.equals(target);
    }

    public boolean hasRightStation(Station target) {
        return right.equals(target);
    }

    public Section merge(Section target) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return left.equals(section.left) && right.equals(section.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
