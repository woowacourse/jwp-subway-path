package subway.repository;

import subway.domain.Section;
import subway.domain.Station;

public interface SectionRepository {
    void save(final Long id, Section section);

    void delete(Section section);

    void deleteSection(Long lineId, Station previousStation, final Long deletedStationId);

    void deleteSection(Long lineId, Long deletedStationId, Station nextStation);
}
