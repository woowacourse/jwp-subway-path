package subway.domain;

import java.util.Objects;

public class Section {
    private Long id;
    private Long upBoundStationId;
    private Long downBoundStationId;
    private Long lineId;
    private Integer distance;

    public Section(Long upBoundStationId, Long downBoundStationId, Long lineId, Integer distance) {
        this.upBoundStationId = upBoundStationId;
        this.downBoundStationId = downBoundStationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public Section(Long id, Long upBoundStationId, Long downBoundStationId, Long lineId, Integer distance) {
        this.id = id;
        this.upBoundStationId = upBoundStationId;
        this.downBoundStationId = downBoundStationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public boolean isUpSection(Long stationId) {
        return this.downBoundStationId.equals(stationId);
    }

    public boolean isDownSection(Long stationId) {
        return this.upBoundStationId.equals(stationId);
    }

    public Long getId() {
        return id;
    }

    public Long getUpBoundStationId() {
        return upBoundStationId;
    }

    public Long getDownBoundStationId() {
        return downBoundStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section that = (Section) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
