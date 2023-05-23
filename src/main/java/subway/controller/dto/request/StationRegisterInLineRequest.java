package subway.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class StationRegisterInLineRequest {

    @NotNull(message = "direction 이 null 입니다.")
    private final SubwayDirection direction;
    @NotBlank(message = "standardStationName 이 비어있습니다.")
    private final String standardStationName;
    @NotBlank(message = "newStationName 이 비어있습니다.")
    private final String newStationName;
    @NotNull(message = "distance 가 null 입니다.")
    @Positive(message = "거리는 양의 정수만 가능합니다.")
    private final Integer distance;

    public StationRegisterInLineRequest(final SubwayDirection direction, final String standardStationName,
                                        final String newStationName, final Integer distance) {
        this.direction = direction;
        this.standardStationName = standardStationName;
        this.newStationName = newStationName;
        this.distance = distance;
    }

    public SubwayDirection getDirection() {
        return direction;
    }

    public String getStandardStationName() {
        return standardStationName;
    }

    public String getNewStationName() {
        return newStationName;
    }

    public Integer getDistance() {
        return distance;
    }
}
