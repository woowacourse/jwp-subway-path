package subway.dto;

import javax.validation.constraints.NotNull;

public class SectionAddRequest {

    @NotNull(message = "추가하려는 역을 입력해주세요")
    private final String sourceStation;
    @NotNull(message = "도착역을 입력해주세요")
    private final String targetStation;
    @NotNull(message = "거리를 입력해주세요")
    private final Integer distance;

    public SectionAddRequest(String sourceStation, String targetStation, Integer distance) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
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
