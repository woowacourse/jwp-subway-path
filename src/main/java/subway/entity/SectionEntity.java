package subway.entity;

import subway.domain.Section;

public class SectionEntity {

    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SectionEntity(final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionEntity of(final Long lineId, final Section section) {
        return new SectionEntity(lineId, section.getUpStation().getId(), section.getDownStation().getId(),
                section.getDistance());
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
        return distance;
    }
}
