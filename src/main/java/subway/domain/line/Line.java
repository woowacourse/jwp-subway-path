package subway.domain.line;

import java.util.Collections;
import subway.domain.section.Sections;

public class Line {

    private final LineName name;
    private final String color;
    private Sections sections;

    public Line(final String name, final String color) {
        this(name, color, new Sections(Collections.emptyList()));
    }

    public Line(final String name, final String color, final Sections sections) {
        this.name = new LineName(name);
        this.color = color;
        this.sections = sections;
    }

    public void updateSections(final Sections sections) {
        this.sections = sections;
    }

    public String getName() {
        return name.name();
    }

    public String getColor() {
        return color;
    }

    public Sections getSections() {
        return sections;
    }
}
