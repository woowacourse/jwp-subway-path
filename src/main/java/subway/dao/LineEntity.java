package subway.dao;

import subway.domain.line.Line;
import subway.domain.section.Section;

import java.util.ArrayList;
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

    public LineEntity(final String name, final String color) {
        this(null, name, color);
    }

    public Line toLine() {
        return new Line(id, name, color, new ArrayList<>());
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
