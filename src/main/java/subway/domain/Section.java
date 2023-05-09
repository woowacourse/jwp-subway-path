package subway.domain;

public class Section {

    private final Station source;
    private final Station target;
    private final int distance;

    public Section(final Station source, final Station target, final int distance) {
        this.source = source;
        this.target = target;
        this.distance = distance;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public int getDistance() {
        return distance;
    }
}
