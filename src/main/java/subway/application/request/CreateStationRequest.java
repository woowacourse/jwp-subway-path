package subway.application.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class CreateStationRequest {

    @NotNull(message = "역명은 null일 수 없습니다.")
    @Length(min = 1, max = 10, message = "역명의 길이는 1 ~ 10이하이어야 합니다.")
    private String stationName;

    public CreateStationRequest() {
    }

    public CreateStationRequest(final String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
