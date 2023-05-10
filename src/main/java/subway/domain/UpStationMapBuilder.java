package subway.domain;

public class UpStationMapBuilder {
    private Long id;
    private Line line;
    private Station startingStation;
    private Station destinationStation;
    private int distance;

    protected UpStationMapBuilder(Long id, Line line, Station startingStation, Station destinationStation, int distance) {
        this.id = id;
        this.line = line;
        this.startingStation = startingStation;
        this.destinationStation = destinationStation;
        this.distance = distance;
    }

    public UpStationMapBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UpStationMapBuilder line(Line line) {
        this.line = line;
        return this;
    }

    public UpStationMapBuilder station(Station station) {
        this.startingStation = station;
        return this;
    }

    public UpStationMapBuilder distance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("노선의 역과 역 사이는 언제나 양의 정수를 유지해야 합니다.");
        }
        this.distance = distance;
        return this;
    }

    public SubwayMap build() {
        return new SubwayMap(id, line, startingStation, destinationStation, distance);
    }
}
