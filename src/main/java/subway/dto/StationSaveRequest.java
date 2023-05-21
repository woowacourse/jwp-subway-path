package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class StationSaveRequest {

    @NotBlank(message = "노선명을 입력해주세요.")
    private final String lineName;

    @NotBlank(message = "기준역명을 입력해주세요.")
    private final String baseStationName;

    @NotBlank(message = "등록할 역명을 입력해주세요.")
    private final String additionalStationName;

    @NotNull(message = "왼쪽으로 등록할 경우 LEFT, 오른쪽으로 등록할 경우 RIGHT로 입력해주세요.")
    private final String direction;

    @Positive(message = "등록할 거리는 양수여야 합니다.")
    private final Integer distance;

    public StationSaveRequest(
            final String lineName,
            final String baseStationName,
            final String additionalStationName,
            final String direction,
            final Integer distance
    ) {
        this.lineName = lineName;
        this.baseStationName = baseStationName;
        this.additionalStationName = additionalStationName;
        this.direction = direction;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getBaseStationName() {
        return baseStationName;
    }

    public String getAdditionalStationName() {
        return additionalStationName;
    }

    public String getDirection() {
        return direction;
    }

    public Integer getDistance() {
        return distance;
    }
}
