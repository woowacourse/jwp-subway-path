package subway.dto;

import subway.domain.Section;

public class SectionResponse {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SectionResponse(final Long id, final Long upStationId, final Long downStationId, final int distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionResponse of(final Long id, final Section section) {
        return new SectionResponse(id, section.getUpStation().getId(), section.getDownStation().getId(), section.getDistance());
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

    public int getDistance() {
        return distance;
    }
}
