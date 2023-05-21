package subway.domain.repository;

import subway.domain.Section;
import subway.domain.Sections;

import java.util.List;

public interface SectionRepository {
    void saveAll(Long lineId, Sections sections);

    List<Section> findAll();

    Sections findAllByLineId(Long lineId);

    void deleteAllByLineId(Long lineId);

    boolean isExistSectionUsingStation(Long stationId);
}
