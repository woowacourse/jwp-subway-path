package subway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.Line;

@Getter
@AllArgsConstructor
public class LineEntity {
    private final Long id;
    private final String name;
    private final String color;

    public LineEntity(String name, String color) {
        this(null, name, color);
    }

    public Line toLine() {
        return new Line(name, color);
    }
}
