package subway.station.persistence;

public class StationEntity {

    private final Long id;
    private final String stationName;

    public StationEntity(String stationName) {
        this(null, stationName);
    }

    public StationEntity(final Long id, final String stationName) {
        this.id = id;
        this.stationName = stationName;
    }

    public Long getId() {
        return id;
    }

    public String getStationName() {
        return stationName;
    }
}
