package subway.dto;

import subway.domain.Line3;

public class LineResponse {
    private Long id;
    private String name;
    private String color;

    public LineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineResponse of(Line3 line) {
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
