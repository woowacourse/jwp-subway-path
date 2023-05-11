package subway.dto.station;

import javax.validation.constraints.NotBlank;

public class StationUpdateRequest {
    @NotBlank(message = "역 이름은 비어있을 수 없습니다.")
    private String stationName;

    public StationUpdateRequest() {
    }

    public StationUpdateRequest(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
