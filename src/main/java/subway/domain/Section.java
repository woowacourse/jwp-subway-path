package subway.domain;

public class Section {
    private final Long id;
    private final Line line;
    private final Station previousStation;
    private final Station nextStation;
    private final int distance;

    public Section(Long id, Line line, Station previousStation, Station nextStation, int distance) {
        this.id = id;
        this.line = line;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public static StationsBuilder builder() {
        return new StationsBuilder();
    }

    public Long getId() {
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
