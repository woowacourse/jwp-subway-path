package subway.dto;

import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotNull
    private String lineName;
    @NotNull
    private String sourceStation;
    @NotNull
    private String destinationStation;
    @NotNull
    private Integer distance;

    public LineRequest(String lineName, String sourceStation, String destinationStation, Integer distance) {
        this.lineName = lineName;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
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

    public String getDestinationStation() {
        return destinationStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
