package subway.business.converter.section;

import subway.business.converter.line.LineConverter;
import subway.business.converter.station.StationEntityDomainConverter;
import subway.business.domain.Distance;
import subway.business.domain.Line;
import subway.business.domain.Section;
import subway.business.domain.Station;
import subway.persistence.entity.SectionDetailEntity;
import subway.presentation.dto.response.SectionResponse;

import java.util.List;
import java.util.stream.Collectors;

public class SectionDomainResponseConverter {

    public static List<SectionResponse> toResponses(final List<Section> sections) {
        return sections.stream()
                .map(SectionDomainResponseConverter::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    private static SectionResponse toResponse(final Section section) {
        return new SectionResponse(
                section.getId(),
                LineConverter.domainToResponseDto(section.getLine()),
                section.getDistance().getLength(),
                StationEntityDomainConverter.domainToResponseDto(section.getPreviousStation()),
                StationEntityDomainConverter.domainToResponseDto(section.getNextStation()));
    }

    public static List<Section> queryResultToDomains(final List<SectionDetailEntity> sectionDetailEntities) {
        return sectionDetailEntities.stream()
                .map(SectionDomainResponseConverter::queryResultToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    private static Section queryResultToDomain(final SectionDetailEntity sectionDetailEntity) {
        return new Section(
                sectionDetailEntity.getId(),
                new Line(sectionDetailEntity.getLineId(), sectionDetailEntity.getLineName(), sectionDetailEntity.getLineColor()),
                new Station(sectionDetailEntity.getPreviousStationId(), sectionDetailEntity.getPreviousStationName()),
                new Station(sectionDetailEntity.getNextStationId(), sectionDetailEntity.getNextStationName()),
                new Distance(sectionDetailEntity.getDistance())
        );
    }

}
