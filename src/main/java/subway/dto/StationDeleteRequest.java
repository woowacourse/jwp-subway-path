package subway.dto;

import javax.validation.constraints.NotBlank;

public class StationDeleteRequest {

    @NotBlank
    private final String lineName;

    @NotBlank
    private final String stationName;

    public StationDeleteRequest(String lineName, String stationName) {
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
