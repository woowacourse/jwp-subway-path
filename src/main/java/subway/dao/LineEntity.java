package subway.dao;

import subway.domain.line.Line;
import subway.domain.section.Section;

import java.util.List;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;

    public LineEntity(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineEntity from(final Line line) {
        return new LineEntity(line.getId(), line.getNameValue(), line.getColorValue());
    }

    public Line toLine(final List<Section> sections) {
        return new Line(id, name, color, sections);
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
