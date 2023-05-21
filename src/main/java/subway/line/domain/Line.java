package subway.line.domain;

import org.jgrapht.graph.WeightedMultigraph;
import subway.section.domain.Direction;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.station.domain.Station;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Line {
    private final String name;
    private final String color;
    private final Long extraCharge;
    private final Sections sections;
    
    public Line(final String name, final String color) {
        this(name, color, 0L, new Sections(new HashSet<>()));
    }
    
    public Line(final String name, final String color, final Set<Section> sections) {
        this(name, color, 0L, new Sections(sections));
    }
    
    private Line(final String name, final String color, final Long extraCharge, final Sections sections) {
        validateNameAndColor(name, color);
        this.name = name;
        this.color = color;
        this.extraCharge = extraCharge;
        this.sections = sections;
    }
    
    private void validateNameAndColor(final String name, final String color) {
        validateNullOrEmpty(name);
        validateNullOrEmpty(color);
    }
    
    private void validateNullOrEmpty(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("노선의 이름이나 색상은 null 또는 빈값일 수 없습니다.");
        }
    }
    
    public void initAddStation(final String leftAdditional, final String rightAdditional, final long distance) {
        sections.initAddStation(leftAdditional, rightAdditional, distance);
    }
    
    public void addStation(
            final String base,
            final Direction direction,
            final String additionalStation,
            final long distance
    ) {
        sections.addStation(base, direction, additionalStation, distance);
    }
    
    public void removeStation(final String station) {
        sections.removeStation(station);
    }
    
    public boolean isSameName(final String targetLineName) {
        return this.name.equals(targetLineName);
    }
    
    public boolean isSameColor(final String lineColor) {
        return this.color.equals(lineColor);
    }
    
    public List<String> getSortedStations() {
        return sections.getSortedStations();
    }
    
    public void addLineToGraph(final WeightedMultigraph<Station, Section> graph) {
        sections.addSectionsToGraph(graph);
    }
    
    public boolean isContainsStation(final String stationName) {
        return sections.isContainsStation(stationName);
    }
    
    public String getName() {
        return name;
    }
    
    public String getColor() {
        return color;
    }
    
    public Long getExtraCharge() {
        return extraCharge;
    }
    
    public Sections getSections() {
        return sections;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Line line = (Line) o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(extraCharge, line.extraCharge) && Objects.equals(sections, line.sections);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, color, extraCharge, sections);
    }
    
    @Override
    public String toString() {
        return "Line{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", extraCharge=" + extraCharge +
                ", sections=" + sections +
                '}';
    }
}
