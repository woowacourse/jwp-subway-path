package subway.section.domain.repository;

import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.section.domain.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

public interface SectionRepository {

    Long insert(final Section section);

    Section findByUpStationId(final Long upStationId);

    Optional<SectionEntity> findOptionalByUpStationId(Long upStationId);

    Section findLeftSectionByStationId(Long stationId);

    Section findRightSectionByStationId(Long stationId);

    List<Section> findAll();


    void deleteById(final Long id);

    Section findById(final Long id);

    Sections findByLineId(Long lineId);
}
