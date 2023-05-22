package subway.dto;

import java.util.List;

import subway.domain.section.Section;

public class LineResponseWithSections {

    private final Long id;
    private final String name;
    private final String color;
    private final List<Section> sections;

    public LineResponseWithSections(final Long id, final String name, final String color, final List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
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

    public List<Section> getSections() {
        return sections;
    }
}
