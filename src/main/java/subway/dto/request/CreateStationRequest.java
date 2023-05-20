package subway.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import subway.dto.SectionDto;

public class CreateStationRequest {

    @NotNull(message = "노선 정보가 입력되지 않았습니다.")
    private final Long lineId;

    @NotEmpty(message = "출발역이 입력되지 않았습니다.")
    private final String sourceStation;

    @NotEmpty(message = "도착역이 입력되지 않았습니다.")
    private final String targetStation;

    @Positive(message = "거리는 1 이상이어야 합니다.")
    private final Integer distance;

    public CreateStationRequest(
            final Long lineId,
            final String sourceStation,
            final String targetStation,
            final Integer distance
    ) {
        this.lineId = lineId;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
    }

    public SectionDto toDto() {
        return new SectionDto(lineId, sourceStation, targetStation, distance);
    }

    public Long getLineId() {
        return lineId;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getTargetStation() {
        return targetStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
