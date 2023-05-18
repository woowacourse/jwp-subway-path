package subway.dao;

import subway.domain.section.Section;

public class SectionEntity {

    private final Long id;
    private final Long fromId;
    private final Long toId;
    private final Integer distance;
    private final Long lineId;

    public SectionEntity(final Long id, final Long fromId, final Long toId, final Integer distance, final Long lineId) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public SectionEntity(final Long fromId, final Long toId, final Integer distance, final Long lineId) {
        this(null, fromId, toId, distance, lineId);
    }

    public static SectionEntity of(final Section section, final Long lineId) {
        return new SectionEntity(section.getId(), section.getFrom().getId(), section.getTo().getId(), section.getDistanceValue(), lineId);
    }

    public Long getId() {
        return id;
    }

    public Long getFromId() {
        return fromId;
    }

    public Long getToId() {
        return toId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
