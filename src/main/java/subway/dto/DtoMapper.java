package subway.dto;

import subway.Entity.SectionEntity;
import subway.domain.Section;

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
}
