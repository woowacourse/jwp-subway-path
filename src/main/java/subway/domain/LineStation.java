package subway.domain;

import java.util.Objects;

public class LineStation {
    private Long id;
    private Long upBoundId;
    private Long downBoundId;
    private Long lineId;
    private Integer distance;

    public LineStation(Long upBoundId, Long downBoundId, Long lineId, Integer distance) {
        this.upBoundId = upBoundId;
        this.downBoundId = downBoundId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public LineStation(Long id, Long upBoundId, Long downBoundId, Long lineId, Integer distance) {
        this.id = id;
        this.upBoundId = upBoundId;
        this.downBoundId = downBoundId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getUpBoundId() {
        return upBoundId;
    }

    public Long getDownBoundId() {
        return downBoundId;
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
        LineStation that = (LineStation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
