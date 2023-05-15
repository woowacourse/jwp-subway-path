package subway.service.domain;

public class Section {

    private final long id;
    private final Line line;
    private final Station previousStation;
    private final Station nextStation;
    // TODO: 원시값 포장
    private final int distance;

    public Section(final long id, final Line line, final Station previousStation,
                   final Station nextStation, final int distance) {
        this.id = id;
        this.line = line;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getPreviousStation() {
        return previousStation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public int getDistance() {
        return distance;
    }
}
