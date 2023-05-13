package subway.dto;

import javax.validation.constraints.NotNull;

public class AddStationRequest {

    @NotNull
    private String lineName;
    @NotNull
    private String sourceStation;
    @NotNull
    private String targetStation;
    @NotNull
    private Integer distance;

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
