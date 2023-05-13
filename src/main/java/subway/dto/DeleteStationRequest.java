package subway.dto;

import javax.validation.constraints.NotNull;

public class DeleteStationRequest {

    @NotNull
    private String lineName;
    @NotNull
    private String stationName;

    public DeleteStationRequest(String lineName, String stationName) {
        this.lineName = lineName;
        this.stationName = stationName;
    }

    public String getLineName() {
        return lineName;
    }

    public String getStationName() {
        return stationName;
    }
}
