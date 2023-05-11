package subway.dto.line;

import java.util.List;
import subway.dto.section.SectionResponse;

public class LineDetailResponse {
    private LineResponse lineResponse;
    private List<SectionResponse> sectionResponses;

    public LineDetailResponse() {
    }

    public LineDetailResponse(LineResponse lineResponse, List<SectionResponse> sectionResponses) {
        this.lineResponse = lineResponse;
        this.sectionResponses = sectionResponses;
    }

    public LineResponse getLineResponse() {
        return lineResponse;
    }

    public List<SectionResponse> getSectionResponses() {
        return sectionResponses;
    }
}
