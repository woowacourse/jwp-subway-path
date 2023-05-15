package subway.business.converter.section;

import subway.business.domain.LineSections;
import subway.business.domain.Section;
import subway.persistence.entity.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

public class SectionEntityDomainConverter {

    public static List<SectionEntity> toEntities(final LineSections sections) {
        return sections.getSections().stream()
                .map(SectionEntityDomainConverter::toEntity)
                .collect(Collectors.toUnmodifiableList());
    }

    private static SectionEntity toEntity(final Section section) {
        if (section.getId() == null) {
            return new SectionEntity(section.getLine().getId(),section.getDistance().getLength(),
                    section.getPreviousStation().getId(), section.getNextStation().getId());
        }
        return new SectionEntity(section.getId(), section.getLine().getId(),section.getDistance().getLength(),
                section.getPreviousStation().getId(), section.getNextStation().getId());
    }

}
