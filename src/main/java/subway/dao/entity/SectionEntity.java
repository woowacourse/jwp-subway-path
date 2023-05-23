package subway.dao.entity;

import subway.domain.vo.Distance;
import subway.domain.Section;
import subway.domain.Station;

import java.util.Objects;

public class SectionEntity {

    private final Long id;
    private final Integer distance;
    private final Boolean isStart;
    private final Long upStationId;
    private final Long downStationId;
    private final Long lineId;

    public SectionEntity(final Integer distance, final Boolean isStart, final Long upStationId, final Long downStationId, final Long lineId) {
        this(null, distance, isStart, upStationId, downStationId, lineId);
    }

    public SectionEntity(final Long id, final Integer distance, final Boolean isStart, final Long upStationId, final Long downStationId, final Long lineId) {
        this.id = id;
        this.distance = distance;
        this.isStart = isStart;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
    }

    public static SectionEntity toEntity(final Section section, final Long lineId) {
        return new SectionEntity(
                section.getId(),
                section.getDistance().getValue(),
                section.getStart(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                lineId
        );
    }

    public Section toDomain(final Station upStation, final Station downStation) {
        return new Section(id, new Distance(distance), isStart, upStation, downStation);
    }

    public Long getId() {
        return id;
    }

    public Integer getDistance() {
        return distance;
    }

    public Boolean getStart() {
        return isStart;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SectionEntity that = (SectionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
