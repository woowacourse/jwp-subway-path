package subway.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Line {
    private Long id;
    private String name;
    private String color;
    private final Sections sections;

    public Line(String name, String color) {
        this(null, name, color, null);
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, new Sections(Collections.emptyList()));
    }

    public Line(Long id, String name, String color, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean hasStationInLine(Station station) {
        return sections.containsStation(station);
    }

    public Optional<Section> findSectionWithEndStation(Station station) {
        return sections.findSectionWithEndStation(station);
    }

    public Optional<Section> findSectionWithStartStation(Station station) {
        return sections.findSectionWithStartStation(station);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getId() {
        return id;
    }

    public List<Section> getSortedSections() {
        return Collections.unmodifiableList(sections.getSortedSections());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Line)) {
            return false;
        }

        Line line = (Line) o;

        if (!Objects.equals(name, line.name)) {
            return false;
        }
        return Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }
}
