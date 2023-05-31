package subway.line.application;

import subway.line.domain.Line;
import subway.line.domain.MiddleSection;
import subway.line.ui.dto.LineDto;
import subway.line.ui.dto.SectionDto;

import java.util.List;
import java.util.stream.Collectors;

class DtoMapper {

    public static LineDto toLineDto(Line line) {
        return new LineDto(toSectionDtos(line.getSections()), line.getName());
    }

    private static List<SectionDto> toSectionDtos(List<MiddleSection> sections) {
        return sections.stream()
                       .map(section -> new SectionDto(
                               section.getUpstreamName(),
                               section.getDownstreamName(),
                               section.getDistance())
                       )
                       .collect(Collectors.toUnmodifiableList());
    }
}
