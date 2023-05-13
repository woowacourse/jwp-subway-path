package subway.repository;

import subway.domain.Section;

public interface SectionRepository {
    void save(final Long id, Section section);

    void delete(Section section);
}
