package subway.dto.request;

import javax.validation.constraints.NotBlank;

public class StationDeleteFromLineRequest {

    @NotBlank(message = "노선을 입력해주세요.")
    private String lineName;

    @NotBlank(message = "역을 입력해주세요.")
    private String deleteStationName;

    private StationDeleteFromLineRequest() {
    }

    public StationDeleteFromLineRequest(final String lineName, final String deleteStationName) {
        this.lineName = lineName;
        this.deleteStationName = deleteStationName;
    }


    public String getLineName() {
        return lineName;
    }

    public String getDeleteStationName() {
        return deleteStationName;
    }
}
