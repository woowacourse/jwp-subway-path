package subway.ui.dto;

import java.util.List;

public class LineDto {

    private final List<SectionDto> sections;
    private final String lineName;

    public LineDto(List<SectionDto> sections, String lineName) {
        this.sections = sections;
        this.lineName = lineName;
    }

    public List<SectionDto> getSections() {
        return sections;
    }

    public String getLineName() {
        return lineName;
    }
}
