package subway.repository;

import subway.domain.Section;

public interface SectionRepository {

    Section save(Section section, Long lineId);
}
