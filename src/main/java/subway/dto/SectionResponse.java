package subway.dto;

import subway.entity.SectionEntity;

public class SectionResponse {

    private Long id;
    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    private SectionResponse() {
    }

    public SectionResponse(final Long id, final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionResponse of(final SectionEntity sectionEntity) {
        return new SectionResponse(
                sectionEntity.getId(),
                sectionEntity.getLineId(),
                sectionEntity.getUpStationId(),
                sectionEntity.getDownStationId(),
                sectionEntity.getDistance()
        );
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
        return distance;
    }
}
