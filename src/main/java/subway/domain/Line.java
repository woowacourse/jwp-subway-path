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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void addSection(final Section newSection) {
        if (sections.isHeadStation(newSection.getNextStation())) {
            sections.addHead(newSection);
            return;
        }
        if (sections.isTailStation(newSection.getBeforeStation())) {
            sections.addTail(newSection);
            return;
        }
        sections.addCentral(newSection);
    }

    public void removeStation(final Station station) {
        if (sections.isHeadStation(station)) {
            sections.removeHead();
            return;
        }
        if (sections.isTailStation(station)) {
            sections.removeTail();
            return;
        }
        sections.removeCentral(station);
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
