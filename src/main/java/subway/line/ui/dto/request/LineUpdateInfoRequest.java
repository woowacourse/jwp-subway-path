package subway.line.ui.dto.request;

import javax.validation.constraints.NotBlank;

public class LineUpdateInfoRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;

    private LineUpdateInfoRequest() {
    }

    public LineUpdateInfoRequest(String name, String color) {
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
