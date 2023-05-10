package subway.infrastructure.persistence.entity;

import subway.domain.Section;

public class SectionEntity {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;
    private final Long lineId;

    public SectionEntity(final Long upStationId, final Long downStationId, final int distance, final Long lineId) {
        this(null, upStationId, downStationId, distance, lineId);
    }

    public SectionEntity(final Long id,
                         final Long upStationId,
                         final Long downStationId,
                         final int distance,
                         final Long lineId) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public static SectionEntity of(final Section section, final Long lineId) {
        return new SectionEntity(
                section.up().id(),
                section.down().id(),
                section.distance(),
                lineId);
    }

    public Long id() {
        return id;
    }

    public Long upStationId() {
        return upStationId;
    }

    public Long downStationId() {
        return downStationId;
    }

    public int distance() {
        return distance;
    }

    public Long lineId() {
        return lineId;
    }
}
