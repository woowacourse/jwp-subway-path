package subway.persistance.entity;

import subway.domain.Section;

public class SectionEntity {
    private final Long sectionId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;
    private final Long lineId;
    private final int listOrder;

    public SectionEntity(final Long sectionId, final Long upStationId, final Long downStationId, final int distance, final Long lineId, final int listOrder) {
        this.sectionId = sectionId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineId = lineId;
        this.listOrder = listOrder;
    }

    public SectionEntity(final Section section, final Long lineId, final int listOrder) {
        this(null, section.getUp().getId(), section.getDown().getId(), section.getDistance().getValue(), lineId, listOrder);
    }

    public Long getSectionId() {
        return sectionId;
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

    public Long getLineId() {
        return lineId;
    }

    public int getListOrder() {
        return listOrder;
    }
}
