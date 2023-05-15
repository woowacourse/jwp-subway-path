package subway.entity;

import java.util.Objects;
import subway.domain.Section;

public class SectionEntity {

    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SectionEntity() {
    }

    public SectionEntity(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionEntity of(Long lineId, Section section) {
        return new SectionEntity(lineId, section.getUpStation().getId(), section.getDownStation().getId(),
                section.getDistance().getValue());
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionEntity sectionEntity = (SectionEntity) o;
        return Objects.equals(lineId, sectionEntity.lineId) && Objects.equals(upStationId,
                sectionEntity.upStationId) && Objects.equals(downStationId, sectionEntity.downStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, upStationId, downStationId);
    }
}
