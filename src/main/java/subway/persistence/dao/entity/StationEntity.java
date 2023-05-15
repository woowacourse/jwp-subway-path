package subway.persistence.dao.entity;

public class StationEntity {
    private final Long stationId;
    private final String name;

    public StationEntity(Long stationId, String name) {
        this.stationId = stationId;
        this.name = name;
    }

    public StationEntity(String name) {
        this(null, name);
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
