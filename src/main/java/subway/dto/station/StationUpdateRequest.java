package subway.dto.station;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import subway.validation.annotation.EndsWith;

public class StationUpdateRequest {
    @NotBlank(message = "역 이름은 비어있을 수 없습니다.")
    @Length(
            max = 10,
            message = "역의 이름은 {max}글자를 초과할 수 없습니다."
    )
    @EndsWith(suffix ="역", message = "역의 이름은 '{suffix}'으로 끝나야 합니다.")
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
