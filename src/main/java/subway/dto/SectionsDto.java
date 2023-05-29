package subway.dto;

import java.util.List;

public class SectionsDto {
    private final List<SectionDto> sections;

    public SectionsDto(final List<SectionDto> sections) {
        this.sections = sections;
    }

    public List<SectionDto> getSections() {
        return sections;
    }
}
