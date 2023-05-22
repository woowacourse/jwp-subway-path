package subway.adapter.in.web.line.dto;

import javax.validation.constraints.NotNull;

public class LineRequest {
    @NotNull
    private String name;
    @NotNull
    private Integer surcharge;

    public LineRequest() {
    }

    public LineRequest(final String name, final Integer surcharge) {
        this.name = name;
        this.surcharge = surcharge;
    }

    public String getName() {
        return name;
    }

    public Integer getSurcharge() {
        return surcharge;
    }
}
