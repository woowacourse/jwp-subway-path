package subway.domain;

public class UpStationsBuilder {
    private Long id;
    private Line line;
    private Station previousStation;
    private Station nextStation;
    private int distance;

    protected UpStationsBuilder(Long id, Line line, Station previousStation, Station nextStation, int distance) {
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

    public UpStationsBuilder distance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("노선의 역과 역 사이는 언제나 양의 정수를 유지해야 합니다.");
        }
        this.distance = distance;
        return this;
    }

    public Section build() {
        return new Section(id, line, previousStation, nextStation, distance);
    }
}
