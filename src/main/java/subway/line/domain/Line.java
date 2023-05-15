package subway.line.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Line {

    private final UUID id;
    private final String name;
    private final Sections sections;

    public Line(final UUID id, final String name) {
        this.id = id;
        this.name = name;
        this.sections = new Sections();
    }

    public Line(final String name, final Section... sections) {
        this(UUID.randomUUID(), name, new Sections(sections));
    }

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

    public Line reverse() {
        return new Line(name, sections.reverse());
    }

    public boolean upTerminalIsEqualTo(final Station station) {
        return upTerminal().equals(station);
    }

    public boolean downTerminalIsEqualTo(final Station station) {
        return downTerminal().equals(station);
    }

    public Station upTerminal() {
        return sections.upTerminal();
    }

    public Station downTerminal() {
        return sections.downTerminal();
    }

    public boolean isEmpty() {
        return sections().isEmpty();
    }

    public boolean contains(final Section section) {
        return sections.contains(section);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Line)) {
            return false;
        }
        final Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public List<Section> sections() {
        return sections.sections();
    }
}
