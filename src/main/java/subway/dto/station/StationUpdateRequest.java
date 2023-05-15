package subway.dto.station;

import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.NotBlank;

public class StationUpdateRequest {
    @NotBlank(message = "역 이름은 비어있을 수 없습니다.")
    private String stationName;

    @JsonCreator
    public StationUpdateRequest(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
