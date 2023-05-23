package subway.application.request;

import javax.validation.constraints.NotBlank;

public class LineRequest {
    @NotBlank
    private final String name;

    public LineRequest(final String name) {
        this.name = name;
    }

    private LineRequest() {
        this(null);
    }

    public String getName() {
        return name;
    }
}
