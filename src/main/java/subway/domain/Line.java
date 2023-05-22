package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Line {

    private final Long id;
    private final LineName name;
    private final LineColor color;
    private final Sections sections;

    public Line(final LineName name, final LineColor color) {
        this(null, name, color, new Sections(new ArrayList<>()));
    }

    public Line(final LineName name, final LineColor color, final Sections sections) {
        this(null, name, color, sections);
    }

    public Line(final Long id, final LineName name, final LineColor color, final Sections sections) {
        validate(name, color, sections);
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    private void validate(final LineName name, final LineColor color, final Sections sections) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("노선명이 필요합니다.");
        }
        if (Objects.isNull(color)) {
            throw new IllegalArgumentException("노선색이 필요합니다.");
        }
        if (Objects.isNull(sections)) {
            throw new IllegalArgumentException("노선의 구간이 필요합니다.");
        }
    }

    public Line addSection(final Section section) {
        Sections updateSections = sections.addSection(section);
        return new Line(id, name, color, updateSections);
    }

    public Line removeStation(final Station station) {
        Sections updateSections = sections.removeStation(station);
        return new Line(id, name, color, updateSections);
    }

    public List<Station> findAllStation() {
        return sections.findAllStationUpToDown();
    }

    public void validateNotDuplicatedStation(final Station station) {
        List<Station> allStations = sections.findAllStationUpToDown();
        Set<String> names = allStations.stream()
                .map(Station::getName)
                .collect(Collectors.toSet());
        if (names.contains(station.getName())) {
            throw new IllegalArgumentException(name.getValue() + "선의 중복된 역명이 존재합니다.");
        }
    }

    public boolean isExistSection(final Section section) {
        return sections.isExistSection(section);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getColor() {
        return color.getValue();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public Station getFirstStation() {
        return sections.findFirstStation();
    }

    public Station getLastStation() {
        return sections.findLastStation();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Line line = (Line) o;
        return Objects.equals(id, line.id)
                && Objects.equals(name, line.name)
                && Objects.equals(color, line.color)
                && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, sections);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name=" + name +
                ", color=" + color +
                ", sections=" + sections +
                '}';
    }
}
