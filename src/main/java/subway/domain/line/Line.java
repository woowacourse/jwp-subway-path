package subway.domain.line;

import subway.domain.section.DirectionStrategy;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import java.util.List;
import java.util.Objects;

public class Line {

    private Long id;
    private String name;
    private Sections sections;

    private Line() {
    }

    public Line(final String name) {
        this.name = name;
    }

    public Line(final String name, final Sections sections) {
        this.name = name;
        this.sections = sections;
    }

    public Line(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Line(final Long id, final String name, final Sections sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public void addSection(final Station existStation, final Station newStation, final DirectionStrategy directionStrategy, final Distance distance) {
        this.sections = sections.add(existStation, newStation, directionStrategy, distance);
    }

    public void delete(final Station station) {
        this.sections = sections.delete(station);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Sections getSections() {
        return sections;
    }

    public List<Section> sections() {
        return sections.getSections();
    }

    public List<Station> stations() {
        return sections.getStations();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final Line line = (Line) other;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sections=" + sections +
                '}';
    }
}
