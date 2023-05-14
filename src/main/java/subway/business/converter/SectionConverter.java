package subway.business.converter;

import subway.business.domain.Line;
import subway.business.domain.Section;
import subway.business.domain.Station;
import subway.persistence.entity.SectionDetailEntity;
import subway.presentation.dto.response.SectionResponse;

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
                StationEntityDomainConverter.domainToResponseDto(section.getPreviousStation()),
                StationEntityDomainConverter.domainToResponseDto(section.getNextStation()));
    }

    public static List<Section> queryResultToDomains(final List<SectionDetailEntity> sectionDetailEntities) {
        return sectionDetailEntities.stream()
                .map(SectionConverter::queryResultToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    private static Section queryResultToDomain(final SectionDetailEntity sectionDetailEntity) {
        return new Section(
                sectionDetailEntity.getId(),
                new Line(sectionDetailEntity.getLineId(), sectionDetailEntity.getLineName(), sectionDetailEntity.getLineColor()),
                new Station(sectionDetailEntity.getPreviousStationId(), sectionDetailEntity.getPreviousStationName()),
                new Station(sectionDetailEntity.getNextStationId(), sectionDetailEntity.getNextStationName()),
                sectionDetailEntity.getDistance()
        );
    }

}
