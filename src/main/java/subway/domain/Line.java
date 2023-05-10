package subway.domain;

import java.util.List;

public class Line {

    private final Long id;
    private final String name;
    private final Sections sections;

    public Line(final String name, final Sections sections) {
        this(null, name, sections);
    }

    public Line(final Long id, final String name, final Sections sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public void addSection(final Section section) {
        sections.addSection(section);
    }

    public void removeStation(final Station station) {
        sections.removeStation(station);
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public List<Section> sections() {
        return sections.sections();
    }
}
