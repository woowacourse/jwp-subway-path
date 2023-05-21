package subway.dto;

import java.util.List;

public class LineDto {

    private final String name;
    private final String color;
    private final List<SectionDto> sections;

    public LineDto(final String name, final String color, final List<SectionDto> sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<SectionDto> getSections() {
        return sections;
    }
}
