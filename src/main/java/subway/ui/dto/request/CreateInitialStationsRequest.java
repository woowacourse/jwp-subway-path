package subway.ui.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateInitialStationsRequest {
    @NotBlank(message = "역 이름은 필수값 입니다.")
    private final String firstStation;
    @NotBlank(message = "역 이름은 필수값 입니다.")
    private final String secondStation;
    @NotNull(message = "역 간의 거리는 필수값 입니다.")
    private final Integer distance;

    public CreateInitialStationsRequest(final String firstStation, final String secondStation, final Integer distance) {
        this.firstStation = firstStation;
        this.secondStation = secondStation;
        this.distance = distance;
    }

    public String getFirstStation() {
        return firstStation;
    }

    public String getSecondStation() {
        return secondStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
