package subway.dto;

import javax.validation.constraints.NotBlank;

public class StationDeleteRequest {

    @NotBlank(message = "노선명을 입력해주세요.")
    private String lineName;

    @NotBlank(message = "역명을 입력해주세요.")
    private String stationName;

    private StationDeleteRequest() {
    }

    public StationDeleteRequest(final String lineName, final String stationName) {
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
