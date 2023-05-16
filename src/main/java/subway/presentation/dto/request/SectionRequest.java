package subway.presentation.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SectionRequest {

    @NotNull(message = "lineName 이 null 이면 안됩니다.")
    private final String lineName;
    @NotNull(message = "direction 이 null 이면 안됩니다.")
    private final String direction;
    @NotNull(message = "standardStationName 이 null 이면 안됩니다.")
    private final String standardStationName;
    @NotNull(message = "newStationName 이 null 이면 안됩니다.")
    private final String newStationName;
    @NotNull(message = "direction 이 null 이면 안됩니다.")
    @Positive(message = "거리는 양의 정수만 가능합니다.")
    private final Integer distance;

    public SectionRequest(final String lineName, final String direction, final String standardStationName,
                          final String newStationName, final Integer distance) {
        this.lineName = lineName;
        this.direction = direction;
        this.standardStationName = standardStationName;
        this.newStationName = newStationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getDirection() {
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
