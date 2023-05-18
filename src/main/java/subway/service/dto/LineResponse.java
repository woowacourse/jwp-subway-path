package subway.service.dto;

import java.util.List;

public class LineResponse {

    private final String lineName;
    private final List<SectionResponse> sections;

    public LineResponse(final String lineName, final List<SectionResponse> sections) {
        this.lineName = lineName;
        this.sections = sections;
    }

    public String getLineName() {
        return lineName;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
