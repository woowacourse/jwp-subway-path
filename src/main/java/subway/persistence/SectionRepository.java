package subway.persistence;

import subway.domain.Section;

public interface SectionRepository {

    Section save(Section section, Long lineId);
}
