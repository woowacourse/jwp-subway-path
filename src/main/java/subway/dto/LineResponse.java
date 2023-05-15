package subway.dto;

import subway.domain.Line;

public class LineResponse {
    private Long id;
    private String name;
    private String color;

    public LineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineResponse(final Line line) {
        this.id = line.getId();
        this.name = line.getNameValue();
        this.color = line.getColorValue();
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getNameValue(), line.getColorValue());
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
