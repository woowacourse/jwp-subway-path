package subway.Entity;

import subway.domain.section.Section;

import java.util.List;
import java.util.stream.Collectors;

public class EntityMapper {

    public static SectionEntity convertToSectionEntity(Section section) {
        return new SectionEntity(
                section.getId(),
                section.getLine().getId(),
                section.getUpward().getId(),
                section.getDownward().getId(),
                section.getDistance()
        );
    }

    public static List<SectionEntity> convertToSectionEntities(List<Section> sections) {
        return sections.stream()
                .map(EntityMapper::convertToSectionEntity)
                .collect(Collectors.toList());
    }
}
