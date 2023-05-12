package subway.domain;

import java.util.List;
import java.util.UUID;

public class Line {

    private final UUID id;
    private final String name;
    private final Sections sections;

    public Line(final String name, final Sections sections) {
        this(UUID.randomUUID(), name, sections);
    }

    public Line(final UUID id, final String name, final Sections sections) {
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

    public int totalDistance() {
        return sections.totalDistance();
    }

    public boolean contains(final Section section) {
        return sections.contains(section);
    }

    public boolean isEmpty() {
        return sections().isEmpty();
    }

    public List<Section> sections() {
        return sections.sections();
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }
}
