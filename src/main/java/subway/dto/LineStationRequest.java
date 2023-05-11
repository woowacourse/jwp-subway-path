package subway.dto;

import subway.domain.Section;

public class LineStationRequest {
    private Long lineId;
    private Long preStationId;
    private Long stationId;
    private Long distance;

    public LineStationRequest(Long lineId, Long preStationId, Long stationId, Long distance) {
        this.lineId = lineId;
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getDistance() {
        return distance;
    }

    public Section toSection() {
        return new Section(lineId, preStationId, stationId, distance);
    }
}
