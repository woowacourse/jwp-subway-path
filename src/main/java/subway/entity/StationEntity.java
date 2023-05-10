package subway.entity;

import subway.domain.station.StationName;

import java.util.Objects;

public class StationEntity {

    private final Long id;
    private final StationName name;
    private final Long lineId;

    public StationEntity(Long id, String name, Long lineId) {
        this.id = id;
        this.name = new StationName(name);
        this.lineId = lineId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getStationName();
    }

    public Long getLineId() {
        return lineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationEntity that = (StationEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(lineId, that.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lineId);
    }

    @Override
    public String toString() {
        return "StationEntity{" +
                "id=" + id +
                ", name=" + name +
                ", lineId=" + lineId +
                '}';
    }
}
