package subway.domain;

public class Section {

    private final Long id;
    private final Station upward;
    private final Station downward;
    private final Distance distance;
    private final Line line;

    private Section(Long id, Station upward, Station downward, Distance distance, Line line) {
        validateSameStations(upward, downward);
        this.id = id;
        this.upward = upward;
        this.downward = downward;
        this.distance = distance;
        this.line = line;
    }

    public static Section of(long id, Station upward, Station downward, int distance, Line line) {
        return new Section(id, upward, downward, Distance.from(distance), line);
    }

    public static Section of(Station upward, Station downward, int distance, Line line) {
        return new Section(null, upward, downward, Distance.from(distance), line);
    }

    private void validateSameStations(Station upward, Station downward) {
        if (upward.equals(downward)) {
            throw new IllegalArgumentException("[ERROR] 구간을 구성하는 역은 동일한 역일 수 없습니다.");
        }
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

    public Line getLine() {
        return line;
    }
}
