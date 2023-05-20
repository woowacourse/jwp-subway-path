package subway.dto.request;

import subway.domain.Line;
import subway.domain.Sections;

import javax.validation.constraints.NotEmpty;

public class LineRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    private String color;

    public LineRequest() {
    }

    public LineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public Line toLine() {
        return new Line(null, name, color, new Sections());
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

}
