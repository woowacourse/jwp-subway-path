package subway.dao.entity;

import subway.domain.*;

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
        return new LineEntity(
                line.getId(),
                line.getName(),
                line.getColor()
        );
    }

    public Line convertToLine(final List<Section> sections) {
        return new Line(
                this.id,
                new LineName(this.name),
                new LineColor(this.color),
                new Sections(sections));
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
