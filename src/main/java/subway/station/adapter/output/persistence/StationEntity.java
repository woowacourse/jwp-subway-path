package subway.station.adapter.output.persistence;

public class StationEntity {
    private final Long stationId;
    private final String name;

    public StationEntity(final String name) {
        this(null, name);
    }

    public StationEntity(final Long stationId, final String name) {
        this.stationId = stationId;
        this.name = name;
    }

    public Long getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

}
