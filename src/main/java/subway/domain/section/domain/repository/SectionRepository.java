package subway.domain.section.domain.repository;

import subway.domain.section.domain.Section;
import subway.domain.section.domain.entity.SectionEntity;

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

}
