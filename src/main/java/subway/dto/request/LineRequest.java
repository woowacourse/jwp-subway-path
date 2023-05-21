package subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class LineRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @PositiveOrZero
    private Integer extraFare;

    public LineRequest(String name, String color, Integer extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getExtraFare() {
        return extraFare;
    }
}
