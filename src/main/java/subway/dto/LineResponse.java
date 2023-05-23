package subway.dto;

import subway.domain.line.Line;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;

    private LineResponse(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineResponse of(final Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
