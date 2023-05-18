package subway.controller.dto.response;

import subway.service.domain.LineProperty;

public class LinePropertyResponse {

    private final Long id;
    private final String name;
    private final String color;

    public LinePropertyResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LinePropertyResponse from(LineProperty line) {
        return new LinePropertyResponse(line.getId(), line.getName(), line.getColor());
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
