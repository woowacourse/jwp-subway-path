package subway.domain;

import java.util.List;

public class Line {

    private final String name;
    private final String color;
    private final List<Section> sections;

    public Line(final String name, final String color, final List<Section> sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public boolean contains(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }

    public boolean containsAll(final Station start, final Station end) {
        return sections.stream()
                .anyMatch(section -> section.containsAll(start, end));
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }
}
