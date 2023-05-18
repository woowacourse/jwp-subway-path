package subway.domain.section.dao;

import subway.domain.section.domain.Direction;
import subway.domain.section.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

public interface SectionDao {

    SectionEntity insert(final SectionEntity sectionEntity);

    void deleteById(final Long id);
    List<SectionEntity> findByLineId(final Long lineId);
    List<SectionEntity> findAll();

    Optional<SectionEntity> findNeighborSection(final Long lineId, final Long baseId, final Direction direction);

    void update(SectionEntity sectionEntity);
}
