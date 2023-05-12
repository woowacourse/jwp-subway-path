package subway.section.application.port.output;

import subway.section.domain.Section;

public interface SectionRepository {
    Long save(Section section, Long lineId);

    void delete(Section removedSection);
}
