package subway.domain;

public class DownStationsBuilder {
    private Long id;
    private Line line;
    private Station previousStation;
    private Station nextStation;
    private Distance distance;

    protected DownStationsBuilder(Long id, Line line, Station previousStation, Station nextStation, Distance distance) {
        this.id = id;
        this.line = line;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public DownStationsBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public DownStationsBuilder line(Line line) {
        this.line = line;
        return this;
    }

    public DownStationsBuilder station(Station station) {
        this.previousStation = station;
        return this;
    }

    public DownStationsBuilder distance(Distance distance) {
        this.distance = distance;
        return this;
    }

    public Section build() {
        return new Section(id, line, nextStation, previousStation, distance);
    }
}
