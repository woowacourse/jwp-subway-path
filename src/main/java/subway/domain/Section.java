package subway.domain;

public class Section {
    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;
    private final Line line;

    public Section(Long id, Station upStation, Station downStation, Distance distance, Line line) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.line = line;
    }

    public Section(Station upStation, Station downStation, Distance distance, Line line) {
        this(null, upStation, downStation, distance, line);
    }

    public boolean contains(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public Distance calculateNewSectionDistance(Distance another) {
        return this.distance.reduce(another);
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Distance getDistance() {
        return distance;
    }

    public Line getLine() {
        return line;
    }
}
