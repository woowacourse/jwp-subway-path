package subway.dto;

import subway.domain.Section;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class LineStationRequest {
    @NotNull
    @Min(value = 0, message = "{value} 이상의 가격을 입력해주세요")
    private Long lineId;
    @NotNull
    @Min(value = 0, message = "{value} 이상의 가격을 입력해주세요")
    private Long preStationId;
    @NotNull
    @Min(value = 0, message = "{value} 이상의 가격을 입력해주세요")
    private Long stationId;
    @NotNull
    @Min(value = 0, message = "{value} 이상의 가격을 입력해주세요")
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
