package subway.dto;

import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotNull
    private String name;
    @NotNull
    private String color;

    public LineRequest() {
    }

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
