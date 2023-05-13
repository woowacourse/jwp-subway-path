package subway.entity;

import java.util.Objects;

public class StationEntity {

    private final Long stationId;
    private final String name;

    public StationEntity(final Long stationId, final String name) {
        this.stationId = stationId;
        this.name = name;
    }

    public StationEntity(final String name) {
        this(null, name);
    }

    public Long getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StationEntity)) {
            return false;
        }
        StationEntity that = (StationEntity) o;
        return Objects.equals(stationId, that.stationId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationId, name);
    }
}
