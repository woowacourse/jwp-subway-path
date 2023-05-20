package subway.dao.entity;

import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.Map;
import java.util.Objects;

public class SectionEntity {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final Long lineId;
    private final Integer distance;

    public SectionEntity(final Long id, final Long upStationId, final Long downStationId, final Long lineId, final Integer distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public static SectionEntity of(final Section section, final Line line) {
        return new SectionEntity(
                section.getId(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                line.getId(),
                section.getDistance().getValue()
        );
    }

    public Section convertToSection(final Map<Long, Station> stations) {
        return new Section(
                this.id,
                stations.get(this.upStationId),
                stations.get(this.downStationId),
                new Distance(this.distance));
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SectionEntity that = (SectionEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(upStationId, that.upStationId)
                && Objects.equals(downStationId, that.downStationId)
                && Objects.equals(lineId, that.lineId)
                && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStationId, downStationId, lineId, distance);
    }

    @Override
    public String toString() {
        return "SectionEntity{" +
                "id=" + id +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", lineId=" + lineId +
                ", distance=" + distance +
                '}';
    }
}
