package subway.repository;

import subway.domain.Line;
import subway.domain.Section;

import java.util.List;

public interface SectionRepository {
    Section save(final Long lineId, Section section);

    void delete(final Long lineId, Section section);

    void deleteSection(Long lineId, Long upStationId, Long downStationId);

    List<Section> findAllSectionOf(Line line);

    Long findStationIdBefore(Long lineId, Long stationId);

    Long findStationIdAfter(Long lineId, Long stationId);

    void deleteSectionsOf(Line line);

    void saveAll(Line line, List<Section> sections);
}
