package subway.dto;

import subway.domain.LineStation;

public class LineStationResponse {
    private Long id;
    private Long upBoundId;
    private Long downBoundId;
    private Long lineId;
    private Integer distance;

    public LineStationResponse(Long upBoundId, Long downBoundId, Long lineId, Integer distance) {
        this.upBoundId = upBoundId;
        this.downBoundId = downBoundId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public LineStationResponse(Long id, Long upBoundId, Long downBoundId, Long lineId, Integer distance) {
        this.id = id;
        this.upBoundId = upBoundId;
        this.downBoundId = downBoundId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public static LineStationResponse of(LineStation lineStation) {
        return new LineStationResponse(lineStation.getId(), lineStation.getUpBoundId(), lineStation.getDownBoundId(), lineStation.getLineId(), lineStation.getDistance());
    }

    public Long getId() {
        return id;
    }

    public Long getUpBoundId() {
        return upBoundId;
    }

    public Long getDownBoundId() {
        return downBoundId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }
}
