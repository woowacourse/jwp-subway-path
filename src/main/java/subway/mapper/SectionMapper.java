package subway.mapper;

import subway.domain.Section;
import subway.entity.SectionEntity;

public class SectionMapper {

    private SectionMapper() {
    }

    public static SectionEntity toEntity(final long lineId, final Section section) {
        return new SectionEntity(lineId, section.getUpStation().getId(), section.getDownStation().getId(),
                section.getDistance());
    }
}
