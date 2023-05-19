package subway.station.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DeleteStationOnTheLineRequest that = (DeleteStationOnTheLineRequest) o;
        return Objects.equals(lineId, that.lineId) && Objects.equals(stationId, that.stationId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(lineId, stationId);
    }
    
    @Override
    public String toString() {
        return "DeleteStationOnTheLineRequest{" +
                "lineId=" + lineId +
                ", stationId=" + stationId +
                '}';
    }
}
