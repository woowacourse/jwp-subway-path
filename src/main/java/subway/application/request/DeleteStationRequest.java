package subway.application.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class DeleteStationRequest {

    @NotNull(message = "역명은 null일 수 없습니다.")
    @Length(min = 1, max = 10, message = "역명의 길이는 1 ~ 10이하이어야 합니다.")
    private String stationName;

    @NotNull(message = "노선명은 null일 수 없습니다.")
    @Length(min = 1, max = 20, message = "노선명 길이는 1 ~ 20이하이어야 합니다.")
    private String lineName;

    public DeleteStationRequest() {
    }

    public DeleteStationRequest(final String stationName, final String lineName) {
        this.stationName = stationName;
        this.lineName = lineName;
    }

    public String getStationName() {
        return stationName;
    }

    public String getLineName() {
        return lineName;
    }
}
