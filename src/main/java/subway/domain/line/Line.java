package subway.domain.line;

import java.util.List;
import java.util.Objects;
import subway.domain.section.Direction;
import subway.domain.section.Distance;
import subway.domain.section.Sections;
import subway.domain.station.Station;

public class Line {

    private final Long id;
    private final LineName name;
    private final LineColor color;
    private final Sections sections;

    public Line(final Long id, final String name, final String color, final Sections sections) {
        this.id = id;
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        this.sections = sections;
    }

    public static Line from(final Line line) {
        return new Line(line.getId(), line.getName(), line.getColor(), Sections.create());
    }

    public static Line of(final Long id, final String name, final String color) {
        return new Line(id, name, color, Sections.create());
    }

    public static Line of(final String name, final String color) {
        return new Line(null, name, color, Sections.create());
    }

    public static Line of(final Line line, final Sections sections) {
        return new Line(line.getId(), line.getName(), line.getColor(), sections);
    }

    public void addSection(
            final Station sourceStation,
            final Station targetStation,
            final Distance distance,
            final Direction direction
    ) {
        sections.addSection(sourceStation, targetStation, distance, direction);
    }

    public void removeSection(final Station targetStation) {
        sections.removeStation(targetStation);
    }

    public List<Station> findStationsByOrdered() {
        return sections.findStationsByOrdered();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    public Sections getSections() {
        return sections;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
