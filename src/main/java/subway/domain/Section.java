package subway.domain;

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

    public Distance getDistance() {
        return distance;
    }
}
