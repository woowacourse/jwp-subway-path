package subway.service.dto;

import java.util.List;

public class LineResponse {

    private final String lineName;
    private final List<SectionResponse> sections;
    private final Long lineId;

    public LineResponse(final String lineName, final List<SectionResponse> sections, final Long lineId) {
        this.lineName = lineName;
        this.sections = sections;
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }

    public Long getLineId() {
        return lineId;
    }
}
