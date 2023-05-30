package subway.dto.request;

import javax.validation.constraints.NotBlank;

public class LineRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String color;

    public LineRequest() {
    }

    public LineRequest(final String name, final String color) {
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
