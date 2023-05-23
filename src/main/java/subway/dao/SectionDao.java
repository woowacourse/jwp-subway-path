package subway.dao;

import java.util.List;
import java.util.Optional;

import subway.domain.section.Section;

public interface SectionDao {

    Section insert(final Section section);

    Optional<Section> findById(final Long id);

    void deleteById(final Long id);

    List<Section> findAll();

    List<Section> findByLineId(final Long lineId);

    Optional<Section> findUpSection(final Long lineId, final Long stationId);

    Optional<Section> findDownSection(final Long lineId, final Long stationId);
}
