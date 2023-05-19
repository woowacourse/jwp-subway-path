package subway.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineRequest {

    @NotBlank(message = "name 이 비어있습니다.")
    private final String name;
    @NotBlank(message = "color 가 비어있습니다.")
    private final String color;
    @NotNull(message = "distance 가 null 입니다.")
    @Positive(message = "거리는 양의 정수만 가능합니다.")
    private final Integer distance;
    @NotBlank(message = "firstStation 이 비어있습니다.")
    private final String firstStation;
    @NotBlank(message = "nextStation 이 비어있습니다.")
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
