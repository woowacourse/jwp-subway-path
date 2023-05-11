package subway.domain;

public final class Path {

    private final Station next;
    private final Long distance;

    public Path(final Station next, final Long distance) {
        this.next = next;
        this.distance = distance;
    }

    public Station getNext() {
        return next;
    }

    public Long getDistance() {
        return distance;
    }
}
