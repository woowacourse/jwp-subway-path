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
}
