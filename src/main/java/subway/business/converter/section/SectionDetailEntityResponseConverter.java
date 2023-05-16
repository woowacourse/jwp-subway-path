package subway.business.converter.section;

import subway.persistence.entity.SectionDetailEntity;
import subway.presentation.dto.response.LineResponse;
import subway.presentation.dto.response.SectionResponse;
import subway.presentation.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class SectionDetailEntityResponseConverter {

    public static List<SectionResponse> toResponses(final List<SectionDetailEntity> sectionDetailEntities) {
        return sectionDetailEntities.stream()
                .map(SectionDetailEntityResponseConverter::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    private static SectionResponse toResponse(final SectionDetailEntity sectionDetailEntity) {
        return new SectionResponse(
                sectionDetailEntity.getId(),
                new LineResponse(sectionDetailEntity.getLineId(), sectionDetailEntity.getLineName(), sectionDetailEntity.getLineColor()),
                sectionDetailEntity.getDistance(),
                new StationResponse(sectionDetailEntity.getPreviousStationId(), sectionDetailEntity.getPreviousStationName()),
                new StationResponse(sectionDetailEntity.getNextStationId(), sectionDetailEntity.getNextStationName())
        );
    }

}
