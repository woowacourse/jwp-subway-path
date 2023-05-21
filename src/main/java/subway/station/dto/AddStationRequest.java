package subway.station.dto;

import subway.section.domain.Direction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddStationRequest {
    @NotNull(message = "lineId는 null일 수 없습니다.")
    private Long lineId;
    @NotBlank(message = "기준역은 null 또는 empty일 수 없습니다.")
    private String baseStation;
    @NotNull(message = "방향은 LEFT 또는 RIGHT만 입력할 수 있습니다.")
    private Direction direction;
    @NotBlank(message = "등록할 역은 null 또는 empty일 수 없습니다.")
    private String additionalStation;
    @NotNull(message = "거리는 null일 수 없습니다.")
    private Long distance;
    
    public AddStationRequest() {}
    
    public AddStationRequest(
            final Long lineId,
            final String baseStation,
            final Direction direction,
            final String additionalStation,
            final Long distance
    ) {
        this.lineId = lineId;
        this.baseStation = baseStation;
        this.direction = direction;
        this.additionalStation = additionalStation;
        this.distance = distance;
    }
    
    public Long getLineId() {
        return lineId;
    }
    
    public String getBaseStation() {
        return baseStation;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public String getAdditionalStation() {
        return additionalStation;
    }
    
    public Long getDistance() {
        return distance;
    }
    
    @Override
    public String toString() {
        return "AddStationRequest{" +
                "lineId=" + lineId +
                ", baseStation='" + baseStation + '\'' +
                ", direction=" + direction +
                ", additionalStation='" + additionalStation + '\'' +
                ", distance=" + distance +
                '}';
    }
}
