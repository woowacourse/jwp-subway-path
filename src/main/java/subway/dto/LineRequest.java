package subway.dto;

import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotNull
    private String lineName;
    @NotNull
    private String sourceStation;
    @NotNull
    private String targetStation;
    @NotNull
    private Integer distance;

    public LineRequest(String lineName, String sourceStation, String targetStation, Integer distance) {
        this.lineName = lineName;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
    }

    public LineRequest() {
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
