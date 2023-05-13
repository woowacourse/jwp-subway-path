package subway.domain;

public class StationsBuilder {
    private Long id;
    private Line line;
    private Station previousStation;
    private Station nextStation;
    private Distance distance;

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

    /**
     * 현재 station을 등록하고, next station을 비워두고 싶을 때 사용합니다.
     * 주로 노선의 하행 종점 역을 등록할 때 사용합니다.
     */
    public UpStationsBuilder nextStationEmpty(Station station) {
        return new UpStationsBuilder(id, line, station, new EmptyStation(), Distance.emptyDistance());
    }

    public UpStationsBuilder before(Station station) {
        this.nextStation = station;
        return new UpStationsBuilder(id, line, previousStation, station, distance);
    }

    public DownStationsBuilder after(Station station) {
        this.nextStation = station;
        return new DownStationsBuilder(id, line, previousStation, station, distance);
    }

    public StationsBuilder distance(Distance distance) {
        this.distance = distance;
        return this;
    }
}
