package subway.domain;

public class Path {

    private final Station next;
    private final int distance;

    public Path(final Station next, final int distance) {
        this.next = next;
        this.distance = distance;
    }

    public boolean isShorterThan(final Integer distance) {
        return this.distance <= distance;
    }

    public Station getNext() {
        return next;
    }

    public int getDistance() {
        return distance;
    }
}
