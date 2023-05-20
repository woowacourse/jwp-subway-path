package subway.dto;

import subway.domain.Section;

public class SectionResponse {
    private Long id;
    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SectionResponse() {
    }

    public SectionResponse(final Long id, final Long lineId, final Long upStationId, final Long downStationId,
                           final Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(section.getId(), section.getLineId(), section.getUpStationId(),
                section.getDownStationId(), section.getDistance());
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

    public Integer getDistance() {
        return distance;
    }
}
