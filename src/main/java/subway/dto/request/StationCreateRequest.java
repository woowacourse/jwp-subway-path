package subway.dto.request;

import javax.validation.constraints.NotBlank;

public class StationCreateRequest {

    @NotBlank(message = "역의 이름은 비어있을 수 없습니다.")
    private String stationName;

    public StationCreateRequest() {
    }

    public StationCreateRequest(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
