package subway.domain.line;

import subway.domain.section.Section;
import subway.domain.section.Sections;

import java.util.List;

public class Line {

    private static final int EMPTY = 0;
    private static final int NOT_EXIST = 0;

    private Long id;
    private Name name;
    private Color color;
    private Sections sections;

    public Line() {
    }

    public Line(final Long id, final Name name, final Color color, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Line (final Long id, final String name, final String color, final List<Section> sections) {
        this(
            id,
            new Name(name),
            new Color(color),
            new Sections(sections)
        );
    }

    public Line (final Long id, final String name, final String color) {
        this(
            id,
            new Name(name),
            new Color(color),
            new Sections(List.of())
        );
    }

    public Line(final String name, final String color) {
        this(
            null,
            new Name(name),
            new Color(color),
            new Sections(List.of())
        );
    }

    public boolean isEmpty() {
        return !sections.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    public Sections getSections() {
        return sections;
    }
}
