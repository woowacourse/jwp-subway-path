package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        this(id, name, new Sections(new ArrayList<>()));
    }

    public Line(final LineName name, final Sections sections) {
        this(null, name, sections);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public Line addSection(final Section newSection) {
        if (sections.isHeadStation(newSection.getNextStation())) {
            final Sections addedSections = sections.addHead(newSection);
            return new Line(name, addedSections);
        }
        if (sections.isTailStation(newSection.getBeforeStation())) {
            final Sections addedSections = sections.addTail(newSection);
            return new Line(name, addedSections);
        }
        final Sections addedSections = sections.addCentral(newSection);
        return new Line(name, addedSections);
    }

    public Line removeStation(final Station station) {
        if (sections.isHeadStation(station)) {
            final Sections removedSections = sections.removeHead();
            return new Line(name, removedSections);
        }
        if (sections.isTailStation(station)) {
            final Sections removedSections = sections.removeTail();
            return new Line(name, removedSections);
        }
        final Sections removedSections = sections.removeCentral(station);
        return new Line(name, removedSections);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
