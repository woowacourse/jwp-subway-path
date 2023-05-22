package subway.dto;

import javax.validation.constraints.NotNull;

public class ShortestPathRequest {

    @NotNull(message = "시작역은 필수로 입력해야 합니다.")
    private String startStationName;
    @NotNull(message = "도착역은 필수로 입력해야 합니다.")
    private String targetStationName;

    public ShortestPathRequest() {
    }

    public ShortestPathRequest(String startStationName, String targetStationName) {
        this.startStationName = startStationName;
        this.targetStationName = targetStationName;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getTargetStationName() {
        return targetStationName;
    }
}
