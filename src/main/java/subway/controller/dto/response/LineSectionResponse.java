package subway.controller.dto.response;

import java.util.ArrayList;
import java.util.List;
import subway.domain.section.PathSection;

public class LineSectionResponse {

    private Long lineId;
    private List<SectionResponse> sections;

    public LineSectionResponse(final Long lineId, final List<SectionResponse> sections) {
        this.lineId = lineId;
        this.sections = sections;
    }

    public static LineSectionResponse from(final List<PathSection> sections) {
        return new LineSectionResponse(sections.get(0).getLineId(), generateSections(sections));
    }

    private static List<SectionResponse> generateSections(final List<PathSection> sections) {
        final List<SectionResponse> result = new ArrayList<>();

        for (final PathSection section : sections) {
            result.add(SectionResponse.from(section));
        }
        return result;
    }

    public Long getLineId() {
        return lineId;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
