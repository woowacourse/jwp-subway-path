package subway.business.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import subway.business.domain.line.Line;

public class LineResponse {
    @Schema(description = "노선 ID")
    private Long id;

    @Schema(description = "노선 이름")
    private String name;

    @Schema(description = "구간 정보")
    private final List<SectionDto> sections;

    private LineResponse(Long id, String name, List<SectionDto> sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public static LineResponse from(Line line) {
        List<SectionDto> sectionDtos = line.getSections().stream()
                .map(SectionDto::from)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), sectionDtos);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SectionDto> getSections() {
        return sections;
    }
}
