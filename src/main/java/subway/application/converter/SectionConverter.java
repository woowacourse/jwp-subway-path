package subway.application.converter;

import subway.application.domain.Line;
import subway.application.domain.Section;
import subway.application.domain.Station;
import subway.dao.rowmapper.SectionDetail;
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

    public static List<Section> queryResultToDomains(final List<SectionDetail> sectionDetails) {
        return sectionDetails.stream()
                .map(SectionConverter::queryResultToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    private static Section queryResultToDomain(final SectionDetail sectionDetail) {
        return new Section(
                sectionDetail.getId(),
                new Line(sectionDetail.getLineId(), sectionDetail.getLineName(), sectionDetail.getLineColor()),
                new Station(sectionDetail.getPreviousStationId(), sectionDetail.getPreviousStationName()),
                new Station(sectionDetail.getNextStationId(), sectionDetail.getNextStationName()),
                sectionDetail.getDistance()
        );
    }

}
