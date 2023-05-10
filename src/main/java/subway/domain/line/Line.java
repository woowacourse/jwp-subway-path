package subway.domain.line;

import java.util.Objects;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.Stations;

public class Line {
    private Long id;
    private Name name;
    private Color color;
    private Stations stations;
    private Sections sections;

    public Line() {
    }

    public Line(final String name, final String color) {
        this(null, name, color, null, null);
    }

    public Line(final Long id, final String name, final String color, final Stations stations, final Sections sections) {
        validate(stations, sections);
        this.id = id;
        this.name = new Name(name);
        this.color = new Color(color);
        this.stations = stations;
        this.sections = sections;
    }

    private void validate(final Stations stations, final Sections sections) {
        if((stations.isEmpty() && sections.isEmpty()) || stations.isCorrectSectionsSize(sections)) {
            return;
        }
        throw new IllegalArgumentException("역의 수에 따른 간선의 수가 올바르지 않습니다.");
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

    public Stations getStations() {
        return stations;
    }

    public Station getUpBoundStation() {
        return stations.getFirstStation();
    }

    public Station getDownBoundStation() {
        return stations.getLastStation();
    }

    public Sections getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
