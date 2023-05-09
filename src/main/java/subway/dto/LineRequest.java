package subway.dto;

import javax.validation.constraints.NotBlank;

public class LineRequest {
    @NotBlank
    private String name;

    public LineRequest() {
    }

    public LineRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
