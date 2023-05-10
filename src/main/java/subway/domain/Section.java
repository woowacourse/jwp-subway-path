package subway.domain;

public class Section {

    private final Long id;
    private final Station source;
    private final Station target;
    private final Distance distance;

    public Section(Station source, Station target, int distance) {
        this(null, source, target, new Distance(distance));
    }

    public Section(Station source, Station target, Distance distance) {
        this(null, source, target, distance);
    }

    public Section(Long id, Station source, Station target, Distance distance) {
        this.id = id;
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

    public Distance getDistance() {
        return distance;
    }
}
