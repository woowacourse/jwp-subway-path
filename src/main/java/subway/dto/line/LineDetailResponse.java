package subway.dto.line;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import subway.dto.section.SectionResponse;

public class LineDetailResponse {
    @JsonProperty("line")
    private LineResponse lineResponse;

    @JsonProperty("sections")
    private List<SectionResponse> sectionResponses;

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