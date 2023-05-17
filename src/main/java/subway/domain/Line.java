package subway.domain;

import java.util.ArrayList;
import java.util.List;
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

    public Section addInitialSection(final Station upStation, final Station downStation, final int distance) {
        return sectionMap.addInitialSection(upStation, downStation, distance);
    }

    public Section addSection(final Station upStation, final Station downStation, final int distance) {
        return sectionMap.addSection(upStation, downStation, distance);
    }

    public void deleteStation(final Station station) {
        sectionMap.deleteStation(station);
    }

    public Station getUpEndstation() {
        return sectionMap.findUpEndstation();
    }

    public List<Section> getSections() {
        return new ArrayList<>(sectionMap.getSectionMap().values());
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

    public SectionMap getSectionMap() {
        return sectionMap;
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
