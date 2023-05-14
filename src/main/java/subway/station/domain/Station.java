package subway.station.domain;

public final class Station {

    private final Long id;
    private final StationName stationName;

    public Station(Long id, String stationName) {
        this.id = id;
        this.stationName = StationName.from(stationName);
    }

    public Station(String stationName) {
        this(null, stationName);
    }

    public String getName() {
        return stationName.getValue();
    }
}
