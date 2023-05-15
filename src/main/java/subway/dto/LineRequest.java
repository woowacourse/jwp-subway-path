package subway.dto;

import subway.domain.Line;

public class LineRequest {

    private String name;
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

    public Line toEntity() {
        return Line.of(name, color);
    }

}
