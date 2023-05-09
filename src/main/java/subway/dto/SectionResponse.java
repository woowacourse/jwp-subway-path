package subway.dto;

import subway.domain.Section;

public class SectionResponse {
    private final Long id;
    private final Long firstStationId;
    private final Long secondStationId;
    private final Long lineId;
    private final Integer distance;

    public SectionResponse(Long id, Section section) {
        this.id = id;
        this.firstStationId = section.getSrc().getId();
        this.secondStationId = section.getTar().getId();
        this.lineId = section.getLine().getId();
        this.distance = section.getDistance().value();
    }

    public Long getId() {
        return id;
    }

    public Long getFirstStationId() {
        return firstStationId;
    }

    public Long getSecondStationId() {
        return secondStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }
}
