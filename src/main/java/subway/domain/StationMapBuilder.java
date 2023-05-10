package subway.domain;

public class StationMapBuilder {
    private Long id;
    private Line line;
    private Station startingStation;
    private Station destinationStation;
    private int distance;

    public StationMapBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public StationMapBuilder line(Line line) {
        this.line = line;
        return this;
    }

    public StationMapBuilder startingStation(Station startingStation) {
        this.startingStation = startingStation;
        return this;
    }

    public UpStationMapBuilder before(Station station) {
        this.destinationStation = station;
        return new UpStationMapBuilder(id, line, startingStation, station, distance);
    }

    public DownStationMapBuilder after(Station station) {
        this.destinationStation = station;
        return new DownStationMapBuilder(id, line, startingStation, station, distance);
    }

    public StationMapBuilder distance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리 정보는 양의 정수로 제한합니다.");
        }
        this.distance = distance;
        return this;
    }
}
