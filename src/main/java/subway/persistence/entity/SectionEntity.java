package subway.persistence.entity;

import subway.domain.Section;

public class SectionEntity {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final long distance;

    public SectionEntity(final Long id, final Long upStationId, final Long downStationId, final long distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionEntity from(final Section section) {
        return new SectionEntity(section.getId(), section.getUpStation().getId(),
            section.getDownStation().getId(), section.getDistance().getValue());
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

    public long getDistance() {
        return distance;
    }
}
