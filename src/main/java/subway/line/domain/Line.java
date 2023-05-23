package subway.line.domain;

import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.vo.Color;
import subway.vo.Name;

import java.util.List;

public class Line {

    private final Long id;
    private final Sections sections;
    private Name name;
    private Color color;

    private Line(final Long id, final Sections sections, final Name name, final Color color) {
        this.id = id;
        this.sections = sections;
        this.name = name;
        this.color = color;
    }

    public static Line of(final Long id, final Sections sections, final Name name, final Color color) {
        return new Line(id, sections, name, color);
    }

    public static Line of(final String name, final String color) {
        return new Line(null, null, Name.from(name), Color.from(color));
    }

    public void updateInfo(final String name, final String color) {
        this.name = Name.from(name);
        this.color = Color.from(color);
    }


    public Long getId() {
        return id;
    }

    public String getNameValue() {
        return name.getValue();
    }

    public String getColorValue() {
        return color.getValue();
    }

    public List<Section> getSectionsValues() {
        return sections.getSections();
    }
}
