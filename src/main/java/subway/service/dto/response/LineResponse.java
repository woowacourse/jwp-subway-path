package subway.service.dto.response;

import java.util.List;

public class LineResponse {

    private final String lineName;
    private final List<SectionInLineResponse> sectionInLineResponses;

    public LineResponse(final String lineName, final List<SectionInLineResponse> sectionInLineResponses) {
        this.lineName = lineName;
        this.sectionInLineResponses = sectionInLineResponses;
    }

    public String getLineName() {
        return lineName;
    }

    public List<SectionInLineResponse> getSectionInLineResponses() {
        return sectionInLineResponses;
    }
}
