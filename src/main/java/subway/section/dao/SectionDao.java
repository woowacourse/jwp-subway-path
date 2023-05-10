package subway.section.dao;

import java.util.List;
import java.util.Optional;

import subway.section.domain.Direction;
import subway.section.entity.SectionEntity;

public interface SectionDao {

    SectionEntity insert(final SectionEntity sectionEntity);

    Optional<SectionEntity> findById(final Long id);

    void deleteById(final Long id);

    List<SectionEntity> findByLineId(final Long lineId);

    Optional<SectionEntity> findNeighborSection(final Long lineId, final Long baseId, final Direction direction);

    Optional<SectionEntity> findNeighborUpSection(final Long lineId, final Long stationId);

    Optional<SectionEntity> findNeighborDownSection(final Long lineId, final Long stationId);
}
