package subway.common.mapper;

import subway.adapter.out.persistence.entity.SectionEntity;
import subway.domain.section.Section;

public class SectionMapper {

    private SectionMapper() {
    }

    public static SectionEntity toEntity(final long lineId, final Section section) {
        return new SectionEntity(lineId, section.getUpStation().getId(), section.getDownStation().getId(),
                section.getDistance());
    }
}
