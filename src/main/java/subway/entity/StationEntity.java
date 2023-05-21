package subway.entity;

import subway.domain.station.StationName;

import java.util.Objects;

public class StationEntity {

    private final Long id;
    private final StationName stationName;
    private final Long lineId;

    public StationEntity(Long id, String stationName, Long lineId) {
        this.id = id;
        this.stationName = new StationName(stationName);
        this.lineId = lineId;
    }

    public Long getId() {
        return id;
    }

    public String getStationName() {
        return stationName.getStationName();
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StationEntity{" +
                "id=" + id +
                ", name=" + stationName +
                ", lineId=" + lineId +
                '}';
    }
}
