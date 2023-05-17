package subway.dto;

import subway.entity.SectionEntity;

public class SectionResponse {

    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    private SectionResponse(final Long upStationId, final Long downStationId, final int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionResponse of(final SectionEntity section) {
        return new SectionResponse(section.getUpStationId(), section.getDownStationId(), section.getDistance());
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
