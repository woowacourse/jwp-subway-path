package subway.presentation.dto;

import subway.application.core.domain.LineProperty;

public class LinePropertyResponse {

    private Long id;
    private String name;
    private String color;

    public LinePropertyResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LinePropertyResponse of(LineProperty line) {
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
