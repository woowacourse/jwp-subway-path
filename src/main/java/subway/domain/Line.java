package subway.domain;

import subway.domain.section.EmptySections;
import subway.domain.section.Section;
import subway.domain.section.Sections;

public class Line {

    private final Long id;
    private final LineName name;
    private final Sections sections;

    public Line(final Long id, final LineName name, final Sections sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public Line(final Long id, final LineName name) {
        this(id, name, new EmptySections());
    }

    public Long getId() {
        return id;
    }

    public LineName getName() {
        return name;
    }

    public Sections getSections() {
        return sections;
    }

    public Line addSection(final Section section) {
        return new Line(id, name, sections.addSection(section));
    }

    public Line removeStation(final Station station) {
        return new Line(id, name, sections.removeStation(station));
    }
}
