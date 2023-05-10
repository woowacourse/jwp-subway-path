package subway.domain;

public class DownStationMapBuilder {
    private Long id;
    private Line line;
    private Station startingStation;
    private Station destinationStation;
    private int distance;

    protected DownStationMapBuilder(Long id, Line line, Station startingStation, Station destinationStation, int distance) {
        this.id = id;
        this.line = line;
        this.startingStation = startingStation;
        this.destinationStation = destinationStation;
        this.distance = distance;
    }

    public DownStationMapBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public DownStationMapBuilder line(Line line) {
        this.line = line;
        return this;
    }

    public DownStationMapBuilder station(Station station) {
        this.startingStation = station;
        return this;
    }

    public DownStationMapBuilder distance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리 정보는 양의 정수로 제한합니다.");
        }
        this.distance = distance;
        return this;
    }

    public SubwayMap build() {
        return new SubwayMap(id, line, destinationStation, startingStation, distance);
    }
}
