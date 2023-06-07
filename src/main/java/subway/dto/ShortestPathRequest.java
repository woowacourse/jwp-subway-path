package subway.dto;

import javax.validation.constraints.NotBlank;

public class ShortestPathRequest {

    @NotBlank
    private String startStationName;

    @NotBlank
    private String endStationName;

    public ShortestPathRequest(String startStationName, String endStationName) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
    }

    private ShortestPathRequest() {
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }


}
