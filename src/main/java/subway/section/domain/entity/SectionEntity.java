package subway.section.domain.entity;

import subway.section.domain.Section;
import subway.vo.Distance;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final Distance distance;

    private SectionEntity(
            final Long id,
            final Long lineId,
            final Long upStationId,
            final Long downStationId,
            final Distance distance
    ) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionEntity of(
            final Long lineId,
            final Long upStationId,
            final Long downStationId,
            final int distance
    ) {
        return new SectionEntity(null, lineId, upStationId, downStationId, Distance.from(distance));
    }

    public static SectionEntity of(
            final Long id,
            final Long lineId,
            final Long upStationId,
            final Long downStationId,
            final int distance
    ) {
        return new SectionEntity(id, lineId, upStationId, downStationId, Distance.from(distance));
    }

    public Section toDomain() {
        return Section.of(id, upStationId, downStationId, distance.getValue());
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

    public int getDistance() {
        return distance.getValue();
    }

}
