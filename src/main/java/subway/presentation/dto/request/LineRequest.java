package subway.presentation.dto.request;

import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotNull(message = "name 이 null 이면 안됩니다.")
    private final String name;
    @NotNull(message = "color 가 null 이면 안됩니다.")
    private final String color;
    @NotNull(message = "distance 가 null 이면 안됩니다.")
    private final Integer distance;
    @NotNull(message = "firstStation 이 null 이면 안됩니다.")
    private final String firstStation;
    @NotNull(message = "nextStation 이 null 이면 안됩니다.")
    private final String secondStation;

    public LineRequest(final String name, final String color, final Integer distance,
                       final String firstStation, final String secondStation) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.firstStation = firstStation;
        this.secondStation = secondStation;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getFirstStation() {
        return firstStation;
    }

    public String getSecondStation() {
        return secondStation;
    }

}
