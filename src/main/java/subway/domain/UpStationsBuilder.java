package subway.domain;

public class UpStationsBuilder {
    private Long id;
    private Line line;
    private Station previousStation;
    private Station nextStation;
    private Distance distance;

    protected UpStationsBuilder(Long id, Line line, Station previousStation, Station nextStation, Distance distance) {
        this.id = id;
        this.line = line;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public UpStationsBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UpStationsBuilder line(Line line) {
        this.line = line;
        return this;
    }

    public UpStationsBuilder station(Station station) {
        this.previousStation = station;
        return this;
    }

    public UpStationsBuilder distance(Distance distance) {
        this.distance = distance;
        return this;
    }

    public Section build() {
        return new Section(id, line, previousStation, nextStation, distance);
    }
}
