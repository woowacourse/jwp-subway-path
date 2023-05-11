package subway.persistence.dao.entity;

public class StationEntity {
    private final long stationId;
    private final String name;

    public StationEntity(long stationId, String name) {
        this.stationId = stationId;
        this.name = name;
    }

    public long getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "StationEntity{" +
                "stationId=" + stationId +
                ", name='" + name + '\'' +
                '}';
    }
}
