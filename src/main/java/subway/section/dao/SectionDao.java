package subway.section.dao;

import subway.section.domain.Direction;
import subway.section.domain.Section;
import subway.section.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

public interface SectionDao {

    SectionEntity insert(final SectionEntity sectionEntity);

    void deleteById(final Long id);

    List<SectionEntity> findByLineId(final Long lineId);

    List<Section> findSectionsByLineId(final Long lineId);

    Optional<SectionEntity> findNeighborSection(final Long lineId, final Long baseId, final Direction direction);
}
