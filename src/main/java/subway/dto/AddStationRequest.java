package subway.dto;

import javax.validation.constraints.NotNull;

public class AddStationRequest {

    @NotNull
    private final String lineName;
    @NotNull
    private final String sourceStation;
    @NotNull
    private final String targetStation;
    @NotNull
    private final Integer distance;

    public AddStationRequest(String lineName, String sourceStation, String targetStation, Integer distance) {
        this.lineName = lineName;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
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
