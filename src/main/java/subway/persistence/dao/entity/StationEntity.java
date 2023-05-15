package subway.persistence.dao.entity;

public class StationEntity {
    private final Long id;
    private final String name;

    public StationEntity(Long stationId, String name) {
        this.id = stationId;
        this.name = name;
    }

    public StationEntity(String name) {
        this(null, name);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "StationEntity{" +
                "stationId=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
