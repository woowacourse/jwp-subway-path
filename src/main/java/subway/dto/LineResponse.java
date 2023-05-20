package subway.dto;

import lombok.Getter;
import subway.domain.Line;

@Getter
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;

    public LineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId(), line.getName().getName(), line.getColor().getColor());
    }
}
