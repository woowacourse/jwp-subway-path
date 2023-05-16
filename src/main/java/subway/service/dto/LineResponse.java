package subway.service.dto;

import java.util.List;

public class LineResponse {

    private final String lineName;
    private final List<SectionResponse> sectionRespons;

    public LineResponse(final String lineName, final List<SectionResponse> sectionRespons) {
        this.lineName = lineName;
        this.sectionRespons = sectionRespons;
    }

    public String getLineName() {
        return lineName;
    }

    public List<SectionResponse> getSectionInLineResponses() {
        return sectionRespons;
    }
}
