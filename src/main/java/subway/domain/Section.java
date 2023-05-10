package subway.domain;

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
}
