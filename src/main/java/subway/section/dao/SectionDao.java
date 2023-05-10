package subway.section.dao;

import java.util.List;
import java.util.Optional;

import subway.section.domain.Direction;
import subway.section.domain.Section;

public interface SectionDao {

    Section insert(final Section section);

    Optional<Section> findById(final Long id);

    void deleteById(final Long id);

    List<Section> findByLineId(final Long lineId);

    Optional<Section> findNeighborSection(final Long lineId, final Long baseId, final Direction direction);

    Optional<Section> findNeighborUpSection(final Long lineId, final Long stationId);

    Optional<Section> findNeighborDownSection(final Long lineId, final Long stationId);
}
