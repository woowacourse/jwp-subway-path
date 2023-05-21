package subway.dto;

import subway.domain.Section;

public class SectionResponse {

    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    private SectionResponse(final Long upStationId, final Long downStationId, final int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionResponse of(final Section section) {
        return new SectionResponse(section.getUpStation().getId(), section.getDownStation().getId(),
                section.getDistance());
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
