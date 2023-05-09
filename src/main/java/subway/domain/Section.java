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

    public boolean sameSectionWith(Section section) {
        return hasStationOf(section.left) && hasStationOf(section.right);
    }

    public boolean isDistanceBiggerThan(Section target) {
        return distance.isBiggerThan(target.distance);
    }

    public boolean includeSection(Section target) {
        return target.right.equals(right) || target.left.equals(left);
    }
}
