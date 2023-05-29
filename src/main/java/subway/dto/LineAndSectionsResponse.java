package subway.dto;

import subway.domain.line.Line;
import subway.domain.section.Section;

import java.util.List;
import java.util.stream.Collectors;

public class LineAndSectionsResponse {

    private LineResponse lineResponse;
    private List<SectionResponse> sectionResponses;

    public LineAndSectionsResponse() {
    }

    public LineAndSectionsResponse(final LineResponse lineResponse, final List<SectionResponse> sectionResponses) {
        this.lineResponse = lineResponse;
        this.sectionResponses = sectionResponses;
    }

    public static LineAndSectionsResponse of(final Line line, final List<Section> sections) {
        final List<SectionResponse> sectionResponses = sections.stream()
                .map(SectionResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return new LineAndSectionsResponse(LineResponse.of(line), sectionResponses);
    }

    public LineResponse getLineResponse() {
        return lineResponse;
    }

    public List<SectionResponse> getSectionResponses() {
        return sectionResponses;
    }
}
