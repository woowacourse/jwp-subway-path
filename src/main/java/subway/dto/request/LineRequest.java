package subway.dto.request;

import javax.validation.constraints.NotBlank;

public class LineRequest {
    @NotBlank
    private final String name;
    @NotBlank
    private final String color;

    public LineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

}
