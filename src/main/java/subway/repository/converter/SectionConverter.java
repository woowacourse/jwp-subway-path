package subway.repository.converter;

import subway.entity.SectionEntity;
import subway.service.domain.Section;

public class SectionConverter {

    private SectionConverter() {
    }

    public static SectionEntity domainToEntity(Long lineId, Section section) {
        return new SectionEntity(
                lineId,
                section.getDistance(),
                section.getPreviousStation().getId(),
                section.getNextStation().getId()
        );
    }

}
