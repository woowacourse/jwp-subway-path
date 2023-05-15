package subway.line.domain;

import static subway.line.exception.line.LineExceptionType.SURCHARGE_IS_NEGATIVE;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import subway.line.exception.line.LineException;

public class Line {

    private final UUID id;
    private final String name;
    private final int surcharge;
    private final Sections sections;

    public Line(final UUID id, final String name, final int surcharge) {
        this.id = id;
        this.name = name;
        this.surcharge = surcharge;
        this.sections = new Sections();
    }

    public Line(final String name, final int surcharge, final Section... sections) {
        this(UUID.randomUUID(), name, surcharge, new Sections(sections));
    }

    public Line(final String name, final int surcharge, final Sections sections) {
        this(UUID.randomUUID(), name, surcharge, sections);
    }

    public Line(final UUID id, final String name, final int surcharge, final Sections sections) {
        validateSurcharge(surcharge);
        this.id = id;
        this.name = name;
        this.surcharge = surcharge;
        this.sections = sections;
    }

    private void validateSurcharge(final int surcharge) {
        if (surcharge < 0) {
            throw new LineException(SURCHARGE_IS_NEGATIVE);
        }
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
        return new Line(name, surcharge, sections.reverse());
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

    public int surcharge() {
        return surcharge;
    }

    public List<Section> sections() {
        return sections.sections();
    }
}
