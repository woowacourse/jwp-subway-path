package subway.line.presentation.dto;

import subway.line.domain.Line;
import subway.section.presentation.dto.response.SectionResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private List<SectionResponse> sectionResponse;

    public LineResponse() {
    }

    private LineResponse(final Long id, final List<SectionResponse> sectionResponse) {
        this.id = id;
        this.sectionResponse = sectionResponse;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(
                line.getId(),
                line.getSectionsValues().stream()
                        .map(SectionResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public List<SectionResponse> getSectionResponse() {
        return sectionResponse;
    }

}
