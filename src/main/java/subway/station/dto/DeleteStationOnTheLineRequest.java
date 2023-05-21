package subway.station.dto;

import javax.validation.constraints.NotNull;

public class DeleteStationOnTheLineRequest {
    @NotNull(message = "lineId는 null일 수 없습니다.")
    private Long lineId;
    @NotNull(message = "stationId는 null일 수 없습니다.")
    private Long stationId;
    
    public DeleteStationOnTheLineRequest() {}
    
    public DeleteStationOnTheLineRequest(final Long lineId, final Long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }
    
    public Long getLineId() {
        return lineId;
    }
    
    public Long getStationId() {
        return stationId;
    }
    
    @Override
    public String toString() {
        return "DeleteStationOnTheLineRequest{" +
                "lineId=" + lineId +
                ", stationId=" + stationId +
                '}';
    }
}
