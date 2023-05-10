package subway.domain;

public class Section {

    private final Long id;
    private final Station upward;
    private final Station downward;
    private final Distance distance;

    private Section(Long id, Station upward, Station downward, Distance distance) {
        this.id = id;
        this.upward = upward;
        this.downward = downward;
        this.distance = distance;
    }

    public static Section of(long id, Station upward, Station downward, int distance) {
        return new Section(id, upward, downward, Distance.from(distance));
    }

    public static Section of(Station upward, Station downward, int distance) {
        return new Section(null, upward, downward, Distance.from(distance));
    }

    public Long getId() {
        return id;
    }

    public Station getUpward() {
        return upward;
    }

    public Station getDownward() {
        return downward;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}
