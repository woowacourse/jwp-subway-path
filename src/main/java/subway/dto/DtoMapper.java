package subway.dto;

import subway.Entity.SectionEntity;
import subway.domain.Section;
import subway.domain.Station;

public class DtoMapper {

    public static SectionEntity convertToSectionEntity(Section section) {
        return new SectionEntity(
                section.getId(),
                section.getUpward().getId(),
                section.getDownward().getId(),
                section.getDistance(),
                section.getLine().getId()
        );
    }

    public static StationResponse convertToStationReponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
