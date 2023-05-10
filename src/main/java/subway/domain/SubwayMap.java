package subway.domain;

public class SubwayMap {
    private final Long id;
    private final Line line;
    private final Station from;
    private final Station to;
    private final int distance;

    public SubwayMap(Long id, Line line, Station from, Station to, int distance) {
        this.id = id;
        this.line = line;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public static StationMapBuilder builder() {
        return new StationMapBuilder();
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getPreviousStation() {
        return from;
    }

    public Station getNextStation() {
        return to;
    }

    public int getDistance() {
        return distance;
    }
}
