package subway.domain;

public class StationsBuilder {
    private Long id;
    private Line line;
    private Station previousStation;
    private Station nextStation;
    private int distance;

    public StationsBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public StationsBuilder line(Line line) {
        this.line = line;
        return this;
    }

    public StationsBuilder startingStation(Station startingStation) {
        this.previousStation = startingStation;
        return this;
    }

    public UpStationsBuilder before(Station station) {
        this.nextStation = station;
        return new UpStationsBuilder(id, line, previousStation, station, distance);
    }

    public DownStationsBuilder after(Station station) {
        this.nextStation = station;
        return new DownStationsBuilder(id, line, previousStation, station, distance);
    }

    public StationsBuilder distance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리 정보는 양의 정수로 제한합니다.");
        }
        this.distance = distance;
        return this;
    }
}
