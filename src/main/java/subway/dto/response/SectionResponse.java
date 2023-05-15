package subway.dto.response;

import subway.domain.Section;

public class SectionResponse {
    private Long id;
    private Long upBoundStationId;
    private Long downBoundStationId;
    private Long lineId;
    private Integer distance;

    public SectionResponse() {
    }

    public SectionResponse(Long id, Long upBoundStationId, Long downBoundStationId, Long lineId, Integer distance) {
        this.id = id;
        this.upBoundStationId = upBoundStationId;
        this.downBoundStationId = downBoundStationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getUpBoundStationId(),
                section.getDownBoundStationId(),
                section.getLineId(),
                section.getDistance());
    }

    public Long getId() {
        return id;
    }

    public Long getUpBoundStationId() {
        return upBoundStationId;
    }

    public Long getDownBoundStationId() {
        return downBoundStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }
}
