package subway.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import subway.domain.section.PathSection;

@Schema(
        description = "구간 응답 정보",
        example = "{\"lineId\": 1, \"sections\": [{\"upwardStationName\": \"잠실역\", \"downwardStationName\": \"잠실새내역\", \"distance\": 5}]}"
)
public class LineSectionResponse {

    @Schema(description = "노선 ID")
    private Long lineId;

    @Schema(description = "구간 정보 목록")
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
