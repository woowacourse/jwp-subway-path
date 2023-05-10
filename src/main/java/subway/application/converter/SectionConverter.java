package subway.application.converter;

import subway.application.domain.Section;
import subway.ui.dto.response.SectionResponse;

import java.util.List;
import java.util.stream.Collectors;

public class SectionConverter {

    public static List<SectionResponse> domainToResponseDtos(final List<Section> sections) {
        return sections.stream()
                .map(SectionConverter::domainToResponseDto)
                .collect(Collectors.toUnmodifiableList());
    }

    private static SectionResponse domainToResponseDto(final Section section) {
        return new SectionResponse(
                section.getId(),
                LineConverter.domainToResponseDto(section.getLine()),
                section.getDistance(),
                StationConverter.domainToResponseDto(section.getPreviousStation()),
                StationConverter.domainToResponseDto(section.getNextStation()));
    }

}
