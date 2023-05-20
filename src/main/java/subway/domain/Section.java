package subway.domain;

import java.util.Objects;

public class Section {
    private Long id;
    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public Section(final Long lineId, final Long upStationId, final Long downStationId, final Integer distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    public Section(final Long id, final Long lineId, final Long upStationId, final Long downStationId, final Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(upStationId, section.upStationId)
                && Objects.equals(downStationId, section.downStationId) && Objects.equals(distance,
                section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStationId, downStationId, distance);
    }
}
