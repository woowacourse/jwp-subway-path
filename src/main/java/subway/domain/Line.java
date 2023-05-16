package subway.domain;

import java.util.Map;
import java.util.Objects;

public class Line {
    private final Long id;
    private final String name;
    private final String color;
    private final SectionMap sectionMap;

    public Line(final String name, final String color) {
        this(null, name, color, new SectionMap());
    }

    public Line(final Long id, final String name, final String color, final SectionMap sectionMap) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sectionMap = sectionMap;
    }

    public Line(final Long id, final String name, final String color) {
        this(id, name, color, new SectionMap());
    }

    public void addInitialSection(final Station upStation, final Station downStation, final int distance) {
        sectionMap.addInitialSection(upStation, downStation, distance);
    }

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        sectionMap.addSection(upStation, downStation, distance);
    }

    public void deleteStation(final Station station) {
        sectionMap.deleteStation(station);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Map<Station, Section> getSectionMap() {
        return sectionMap.getSectionMap();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }
}
